import { useContext, useEffect, useState } from "react";
import AuthContext from "../../context/AuthContext";
import { Link, useNavigate } from "react-router-dom";
import UploadImageToS3WithNativeSdk from "../../ImageUpload/FileUploadSample";




const RecipeForm = (props) => {


    const auth = useContext(AuthContext);

    const [errors, setErrors] = useState([])

    const navigate = useNavigate();

    const [title, setTitle] = useState("")
    const [steps, setSteps] = useState("")
    const [imageUrl, setImageUrl] = useState("https://plus.unsplash.com/premium_photo-1663852296771-42fa57b3044b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=687&q=80%22")
    const [cookTimeMin, setCookTimeMin] = useState(0)
    const [userId, setUserId] = useState(0);
    const [selectedIngredient, setSelectedIngredient] = useState("")
    const [selectedQuantity, setSelectedQuantity] = useState("");
    const [selectedMeasurement, setSelectedMeasurement] = useState("")
    const [recipeIngredientList, setRecipeIngredientList] = useState([])
    const [measurements, setMeasurements] = useState([]);
    const [ingredients, setIngredients] = useState([]);

    const [filteredIngredients, setFilteredIngredients] = useState([]);


    useEffect(() => {
      filteredMasterList()
    }, [recipeIngredientList])

    useEffect(() => {
      fetch("http://localhost:8080/api/ingredients")
        .then(response => response.json())
        .then(data => {
          setIngredients(data);
          console.log("Ingredients data:", data); // see the ingredients being fetched bc they're blank???
        })
        .catch(error => console.error("Error fetching ingredients:", error));
    }, []);

    useEffect(() => {
      fetch("http://localhost:8080/api/measurements")
        .then(response => response.json())
        .then(data => {
          setMeasurements(data);
          console.log("Measurement data:", data); // see the ingredients being fetched bc they're blank???
        })
        .catch(error => console.error("Error fetching ingredients:", error));
    }, []);


      // Add the current selected ingredient to the list
      const handleAddIngredient = () => {
        if (selectedIngredient && selectedQuantity !== "" && !recipeIngredientList.some(item => item.ingredient === selectedIngredient)) {
          setRecipeIngredientList([...recipeIngredientList, { ingredientId: selectedIngredient, quantity: selectedQuantity, measurementId: selectedMeasurement}]);
          setSelectedIngredient("");
          setSelectedQuantity("");
          setSelectedMeasurement("");
        }
      };
    // Submission handler 
    const handleSubmit = async (event) => {
        event.preventDefault();

        const newRecipe = {
            title, steps, image:imageUrl, cookTimeMin, userId, recipeIngredientList
        }
    
          fetch("http://localhost:8080/api/recipes", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",              
              Authorization: "Bearer " + auth.user.token
            },
            body: JSON.stringify(newRecipe)
          })
          .then(response => {
            if (response.ok) {
              response.json()
              .then(q => {
                // debugging statement
                console.log("API response:", q)
                setTitle(q.title)
                setSteps(q.steps)
                setImageUrl(q.image)
                setCookTimeMin(q.cookTimeMin)
                setUserId(q.userId)
                setRecipeIngredientList(q.recipeIngredientList)
                navigate(`/recipes`)
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

        const filteredMasterList = () => {
          const result = ingredients.filter(i => !recipeIngredientList.some(ri => ri.ingredientId == i.ingredientId))
          setFilteredIngredients(result)
        }
  

    return (
      <div className="recipeadd-container">
      <form className="recipeadd-centered" onSubmit={handleSubmit}>

      <h2 className="recipeadd-header">Fill Out Your Recipe</h2>
        <ul>
          {errors.map(error => <li key={error} style={{color: "red"}}>{error}</li>)}
        </ul>

        <fieldset>
          <label className="recipeadd-title" htmlFor="title-input">Recipe Name: </label>
          <input className="recipeadd-inputbox" id="title-input" value={title} onChange={(evt) => setTitle(evt.target.value)}/>
        </fieldset>

        <fieldset>
          <label className="recipeadd-title" htmlFor="steps-input">Recipe Steps: </label>
          <textarea className="recipeadd-inputboxv2"id="steps-input" value={steps} onChange={(evt) => setSteps(evt.target.value)}/>
        </fieldset>

        <fieldset>
          <label className="recipeadd-title" htmlFor="image-input">Recipe Result Image: </label>
          <UploadImageToS3WithNativeSdk setImage={setImageUrl}/>
        </fieldset>

        <fieldset>
          <label className="recipeadd-title" htmlFor="cook-time-input">Cook Time (Minutes): </label>
          <input className="recipeadd-inputboxv3" type="number" id="cook-time-input" value={cookTimeMin} onChange={(evt) => setCookTimeMin(evt.target.value)}/>
        </fieldset>

        <fieldset>

          <label className="recipeadd-title" htmlFor="ingredients">Select Ingredients:</label>

          <select
            className="recipeadd-inputboxv4"
            name="ingredients"
            id="ingredients"
            value={selectedIngredient}
            onChange={(evt) => setSelectedIngredient(evt.target.value)}
          >
          <option value="">Select an Ingredient</option>
{recipeIngredientList.length === 0
  ? ingredients.map((ingredient) => (
      <option key={ingredient.ingredientId} value={ingredient.ingredientId}>
        {ingredient.ingredientName}
      </option>
    ))
  : filteredIngredients.map((ingredient) => (
      <option key={ingredient.ingredientId} value={ingredient.ingredientId}>
        {ingredient.ingredientName}
      </option>
    ))}
          </select>

          <input
          className="recipeadd-inputboxv4" 
            type="number"
            placeholder="Quantity"
            value={selectedQuantity}
            onChange={(evt) => setSelectedQuantity(evt.target.value)}
          />

          <select
          className="recipeadd-inputboxv4"
            name="measurements"
            id="measurements"
            value={selectedMeasurement}
            onChange={(evt) => setSelectedMeasurement(evt.target.value)}
          >
          <option value="">Select a Measurement</option>
            {measurements.map(measurement => (
          <option key={measurement.measurementId} value={measurement.measurementId}>
            {measurement.measurementName}
          </option>
          ))}
          </select>

          <ul>
            {recipeIngredientList.map((ingredient, index) => (
            <li key={index}>
            Ingredient: {ingredients.find(i => (i.ingredientId == ingredient.ingredientId)).ingredientName} - Quantity: {ingredient.quantity} -
            Measurement: {measurements.find(m => (m.measurementId == ingredient.measurementId)).measurementName}
            </li>
            ))}
          </ul>

          <button className="recipeadd-addbutton" type="button" id="addButton" onClick={handleAddIngredient}>Add Ingredient</button>

        </fieldset>

        <div className="button-group">
        <button type="submit" className="submitrecipe-button">
          Post Recipe
        </button>
        <Link to="/recipes">
          <button type="submit" className="submitrecipe-button">
            Cancel
          </button>
          {/* <Button variant="danger" type="btn">Cancel</Button> */}
        </Link>
      </div>

    </form>
  </div>
);
            }
  
export default RecipeForm;