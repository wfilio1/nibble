import React ,{useState} from 'react';
import AWS from 'aws-sdk'
import dotenv from 'dotenv';

// dotenv.config();

//resources
//note: make sure to npm install aws-sdk
//https://www.youtube.com/watch?v=yGYeYJpRWPM

// https://stackoverflow.com/questions/68438645/how-to-upload-files-to-aws-s3-bucket-from-the-react-frontend
//https://stackoverflow.com/questions/71080354/getting-the-bucket-does-not-allow-acls-error

//AWS configurations
const region = "us-east-2";
const bucketName = "capstone-image-upload-bucket";
const accessKeyId = "AKIAYGE64QJHPQNZFH5G";
const secretAccessKey = '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';


//updates the AWS configuration with the provided access key and secret access key
AWS.config.update({
    accessKeyId,
    secretAccessKey
})

//an instance of the AWS S3 service is created with the provided configuration. The signatureVersion is set to 'v4' to use version 4 of AWS Signature, which is required for certain S3 features.
const s3 = new AWS.S3({
    region,
    accessKeyId,
    secretAccessKey,
    signatureVersion: 'v4'
})

const UploadImageToS3WithNativeSdk = (props) => {

    const [selectedFile, setSelectedFile] = useState(null);
    const [imageUploaded, setImageUploaded] = useState(false);


     // File input change handler
    const handleFileInput = (e) => {
        setSelectedFile(e.target.files[0]);
        setImageUploaded(false);
    }

    // File upload function
    async function uploadFile(file) {
    
        const params = ({
            Bucket: bucketName,
            Key: file.name,
            Expires: 60
        })

        
        //gets a secure URL for uploading the file to S3
        //we will need to use this URL to create a fetch/Put request so we can post the image to our S3 Bucket
        const uploadURL = await s3.getSignedUrlPromise('putObject', params)
    
        console.log(uploadURL);

        // Perform the actual upload using fetch
        await fetch(uploadURL, {
            method: "PUT",
            headers: {
                "Content-Type": file.type
            },
            body: file
        })

        setImageUploaded(true);


        //this is the actual image url from the s3 bucket
        // Extract the uploaded image URL from the S3 URL
        const imageURL = uploadURL.split('?')[0];
        props.setImage(imageURL)
        console.log(imageURL); 

        //now, store this imageURL to the database.

    }


    // set disabled to imageUploaded so when true button is not clickable
    return <div>
        <input className="recipeadd-imagebutton" type="file" onChange={handleFileInput} disabled={imageUploaded} />
        <button className="recipeadd-imagebuttonv2" onClick={() => uploadFile(selectedFile)} disabled={imageUploaded} style={{ marginLeft: '50px' }}> Upload Image</button>
    </div>
}

export default UploadImageToS3WithNativeSdk;