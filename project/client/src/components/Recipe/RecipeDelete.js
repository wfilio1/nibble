import { useContext, useEffect, useState } from "react"
import { useNavigate, useParams, Link } from "react-router-dom"
import AuthContext from "../../context/AuthContext";

function RecipeDelete() {
  const params = useParams()
  const navigate = useNavigate()
  const auth = useContext(AuthContext);
  const token = localStorage.getItem("appUserToken")
  console.log(token)


  const [recipe, setRecipe] = useState(null)

  useEffect(() => {
    console.log(auth.username)
    console.log("Params:", params); // Log the params object
    // ... rest of the code
  }, [params.id]);

  useEffect(() => {
    fetch(`http://localhost:8080/api/recipes/${params.id}`)
    .then(response => {
      if (response.ok) {
        response.json()
        .then(setRecipe)
      } else {
        // we didn't find a recipe for some reason
        navigate('/not-found')
      }
    })
  }, [params.id])

  const handleDelete = () => {
    fetch(`http://localhost:8080/api/recipes/${params.id}`, {
      method: "DELETE",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
    .then(response => {
      if (response.ok) {
        navigate("/recipes")
      } else {
        console.log(`Unexpected response status code: ${response.status}`)
        console.log(params.id)
      }
    })
  }

  // if we don't have a recipe yet then don't attempt to render the confirmation information
  if (recipe === null) {
    return null;
  }

  return (
    <>
    <div className="recipedeletecontainer">
      <h2 className="recipedeleteheader">Confirm Delete</h2>
      <p className="recipedeletebody">Delete this recipe?</p>
      <ul>
        <li className="recipedeletebody2">Recipe: {recipe.title}</li>

      </ul>
      <div className="deletebutton-container">
      <button className="deleterecipe-button" onClick={handleDelete}>Delete</button>
      <Link to="/recipes" className="deletecancel-button">Cancel</Link>
    </div>
  </div>
</>
  );
}

export default RecipeDelete;