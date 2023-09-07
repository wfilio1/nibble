import { useContext } from "react";
import { Button } from "react-bootstrap";
import AuthContext from "../../context/AuthContext";

const AddLikedRecipes = (props) => {

    //when user clicks button, make a post request /api/liked with recipeId
    //and userId

    const auth = useContext(AuthContext);

    const recipeId = props.recipeId;

    const userId = 0;


    const handleLike = () => {

        const likedRecipe = {
            recipeId, userId
        }


        const url = "http://localhost:8080/api/liked";

        fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
                Authorization: "Bearer " + auth.user.token,
            },
            body: JSON.stringify(likedRecipe),
        })
        .then(response => {
            if (response.ok) {
                return response.json(); 
            } else {
                throw new Error("Failed to add recipe to favorites");
            }
        })
        .then(r => {
            props.setLikedRecipeId(r.likedRecipeId);
            props.setLiked(true);
            alert("You've added this recipe to your favorites!");
        })
        .catch(error => {
            console.error("Error adding recipe to favorites:", error);
        
        });
    }

    const handleUnlike = () => {
        const url = `http://localhost:8080/api/liked/${props.likedRecipeId}`;
       
        fetch(url, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
            Authorization: "Bearer " + auth.user.token,
        },
        })
        .then((response) => {
            if (response.ok) {
                props.setLiked(false);
                console.log("unliked");
            } else {
                throw new Error("Failed to unlike the recipe");
            }
        })
        .catch((error) => {
            console.error("Error unliking the recipe:", error);
        });

    }
    
    return(
        <div>
            {props.liked ? <Button onClick={handleUnlike}>&#128154;</Button>
            : <Button onClick={handleLike}>&#129293;</Button>}
        </div>
    );

}

export default AddLikedRecipes;