import { useContext, useEffect, useState } from "react";
import AuthContext from "../../context/AuthContext";
import { Button, Table } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const Comments = (props) => {

    const auth = useContext(AuthContext);

    const navigate = useNavigate();


    const [errors, setErrors] = useState([]);
    const [comments, setComments] = useState([]);

    const [commentInput, setCommentInput] = useState("");
    const [appUserId, setAppUserId] = useState(0);
    const [recipeId, setRecipeId] = useState(parseInt(props.recipeId));

    const loadComments = () => {
        const url = `http://localhost:8080/api/comments/${props.recipeId}`;

        fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json"
            }
        } )
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to fetch comments');
            }
        })
        .then(allComments => {
            setComments(allComments);
        })
    }

    const handleAddComment = async (event) => {
        event.preventDefault();

        const newComment = {
            commentInput, appUserId, recipeId
        }
    
          fetch("http://localhost:8080/api/comments", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",              
              Authorization: "Bearer " + auth.user.token
            },
            body: JSON.stringify(newComment)
          })
          .then(response => {
            if (response.ok) {
              response.json()
              .then(q => {
                // debugging statement
                console.log("API response:", q)
                navigate(`/recipes/${props.recipeId}`)
                setCommentInput("");
              })
            } else {
              response.json()
              .then(errors => {
                if (Array.isArray(errors)) {
                  setErrors(errors)
                } else {
                  setErrors([errors])
                }
              })
            }
          })
        }

    useEffect(() => {
        loadComments();
    }, [comments])

    return (
        <div className="comment-view-container">
            <div className="comment-section">
                <h2>Comments</h2>
                    {errors.map(e => {
                        <p>e</p>
                    })}
                    
                    <Table>
                        <tbody>
                            {comments.map(c =>
                            <tr className="recipeingredients">
                                <td >{c.username}: </td> 
                                <td>{c.commentInput}</td>
                            </tr>)}
                        </tbody>
                    </Table>
                    <form onSubmit={handleAddComment}>
                        <fieldset>
                            <textarea className="recipeadd-inputboxv2" id="steps-input"  value={commentInput} onChange={(evt) => setCommentInput(evt.target.value)}/>
                        </fieldset>
                        <fieldset><Button type="submit">Add a comment</Button></fieldset>
                    </form>
            </div>
 
        </div>
    );
}

export default Comments;