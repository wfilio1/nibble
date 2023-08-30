import React, { useContext, useState, useEffect } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import AuthContext from "../../context/AuthContext";

const AddPantryIngredient = (props) => {
  const params = useParams();
  const navigate = useNavigate();
  const auth = useContext(AuthContext);

  const [errors, setErrors] = useState([]);
  const [quantity, setQuantity] = useState("");
  const [selectedIngredient, setSelectedIngredient] = useState("");
  const [userId, setUserId] = useState(0);
  const [selectedMeasurement, setSelectedMeasurement] = useState("");

  const [ingredients, setIngredients] = useState([]);
  const [measurements, setMeasurements] = useState([]);

  const [filteredIngredients, setFilteredIngredients] = useState([]);
  

  useEffect(() => {
    fetchIngredients();
    fetchMeasurements();
  }, []);
  

  useEffect(() => {
    if (ingredients.length > 0 && props.pantryIngredients.length > 0) {
      loadFilteredIngredients();
    }
  }, [props.pantryIngredients]);


  const fetchIngredients = () => {
    fetch("http://localhost:8080/api/ingredients", {
      headers: {
        Authorization: `Bearer ${auth.user.token}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
          setIngredients(data);
      })
      .catch((error) => {
        console.error("Error fetching ingredients:", error);
      });
  };

  const fetchMeasurements = () => {
    fetch("http://localhost:8080/api/measurements", {
      headers: {
        Authorization: `Bearer ${auth.user.token}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setMeasurements(data); 
      })
      .catch((error) => {
        console.error("Error fetching measurements:", error);
      });
  };

  const loadFilteredIngredients = () => {
        const result = ingredients.filter(i => !props.pantryIngredients.some(pi => pi.ingredientId === i.ingredientId))
        setFilteredIngredients(result);
  }
  


  const handleAdd = async (evt) => {

    if(errors !== null) {
      evt.preventDefault();
      setErrors([])
    }

    const newIngredient = {
      quantity,
      userId,
      ingredientId: selectedIngredient,
      measurementId: selectedMeasurement
    }



    const verb = "POST";
    const url = `http://localhost:8080/api/pantry`;


 
    fetch(url, {
      method: verb,
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization: "Bearer " + auth.user.token,
      },
      body: JSON.stringify(newIngredient),
    })
      .then((response) => {
        if (response.ok) {
          response.json()
          .then (p => {
          console.log("checker:", p)
          props.loadIngredients();
          reset();
        })
       } else {
              response.json()
              .then(errors => {
                if (Array.isArray(errors)) {
                  setErrors(errors)
                } else {
                  setErrors([errors])
                }
          });
        }
      });
  };


  const reset = () => {
    console.log("working")
    setQuantity("");
    setSelectedIngredient("");
    setSelectedMeasurement("");
    setErrors([]);
  };

  return ( 

    <form onSubmit={handleAdd} className="centered-form"> 


      <fieldset className="ingredient-fieldset"> 
      <ul>
        {errors?.map(error => <li key={error} style={{color: "red"}}>{error}</li>)}
      </ul>
        <label htmlFor="ingredient-dropdown">Select Ingredient: </label> 
        <select 
          id="ingredient-dropdown" 
          value={selectedIngredient} 
          onChange={(evt) => setSelectedIngredient(evt.target.value)} 
        > 
          <option value="">Select an Ingredient</option> 
          {filteredIngredients?.map((ingredient) => ( 
            <option key={ingredient.ingredientId} value={ingredient.ingredientId}> 
              {ingredient.ingredientName} 
            </option> 
          ))} 
        </select> 
      </fieldset> 

      <fieldset className="quantity-fieldset"> 
        <label htmlFor="quantity-input">Quantity: </label> 
        <input 
          id="quantity-input" 
          type="number" 
          value={quantity} 
          onChange={(evt) => setQuantity(evt.target.value)} 
        /> 
      </fieldset> 

      <fieldset className="measurement-fieldset"> 
        <label htmlFor="measurement-dropdown">Select Measurement: </label> 
        <select 
          id="measurement-dropdown" 
          value={selectedMeasurement} 
          onChange={(evt) => setSelectedMeasurement(evt.target.value)}  
      > 
          <option value="">Select Measurement</option> 
          {measurements?.map((measurement) => ( 
            <option key={measurement.measurementId} value={measurement.measurementId}> 
              {measurement.measurementName} 
            </option> 
          ))} 
      </select> 
      </fieldset> 
      
      <div className="button-container"> 
        <button type="submit">Add Ingredient</button> 
      </div> 
    </form> 

  ); 
}; 

 
 

export default AddPantryIngredient; 
  