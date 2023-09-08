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
        const url = `${process.env.REACT_APP_EB_BACKEND}/api/comments/${props.recipeId}`;

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
    
          fetch(`${process.env.REACT_APP_EB_BACKEND}/api/comments`, {
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

        const handleDelete = (evt, commentId) => {
          evt.preventDefault();

          const url = `${process.env.REACT_APP_EB_BACKEND}/api/comments/${commentId}`;
       
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

              } else {
                  throw new Error("Failed to delete the recipe");
              }
          })
          .catch((error) => {
              console.error("Error deleting the recipe:", error);
          });

        }


        //check if the comment was created by the logged in user
    const checkUser = (user) => {
      if(auth.user && auth.user.username === user) {
          return true;
      }
      else {
          return false;
      }
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
                                {checkUser(c.username) ? 
                                <td><Button onClick={(evt) => handleDelete(evt, c.commentId)}>Delete</Button></td>
                                : <td></td>}
                                
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