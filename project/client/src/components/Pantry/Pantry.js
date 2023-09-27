import { useContext, useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import AuthContext from "../../context/AuthContext";

import AddPantryIngredient from './AddPantryIngredient';
import DeletePantryIngredient from './DeletePantryIngredient';
import { Button } from "react-bootstrap";

const Pantry = (props) => {
    const auth = useContext(AuthContext);

    const navigate = useNavigate();
  
    const [pantryIngredients, setPantryIngredients] = useState([]);
    const [ingredientMap, setIngredientMap] = useState({});
    const [measurementMap, setMeasurementMap] = useState({});
  
    useEffect(() => {
      if (!auth.user) {
          navigate('/login');
      } else {
          loadIngredients();
          loadAllIngredients();
          loadMeasurements();
      }
  }, [auth.user]);
  
    const loadIngredients = () => {
      fetch(`${process.env.REACT_APP_EB_BACKEND}/api/pantry/personal`, {
        headers: {
          Authorization: `Bearer ${auth.user.token}`,
        },
      })
        .then((response) => response.json())
        .then((data) => {
          setPantryIngredients(data);
          console.log(data);
        })
        .catch((error) => {
          console.error("Error fetching pantry ingredients:", error);
        });
    };

    const loadAllIngredients = () => {
      fetch(`${process.env.REACT_APP_EB_BACKEND}/api/ingredients`, {
          headers: {
              Authorization: `Bearer ${auth.user.token}`,
          },
      })
      .then((response) => response.json())
      .then((data) => {
          const ingredientMap = {};
          data.forEach((ingredient) => {
              ingredientMap[ingredient.ingredientId] = ingredient.ingredientName;
          });
          setIngredientMap(ingredientMap);
      })
      .catch((error) => {
          console.error("Error fetching all ingredients:", error);
      });
  };

  const loadMeasurements = () => {
    fetch(`${process.env.REACT_APP_EB_BACKEND}/api/measurements`, {
      headers: {
        Authorization: `Bearer ${auth.user.token}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
        const measurementMap = {};
        data.forEach((measurement) => {
          measurementMap[measurement.measurementId] = measurement.measurementName;
        });
        setMeasurementMap(measurementMap);
      })
      .catch((error) => {
        console.error("Error fetching measurements:", error);
      });
  };

  return (
    <div className="centered-container">
      <div className="white-box">
        <h2 className="pantry-title">YOUR PANTRY</h2>

        <table className="custom-table">
          <thead>
            <tr>
              <th>INGREDIENT</th>
              <th>QUANTITY</th>
              <th>MEASUREMENT</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {pantryIngredients.map((ingredient) => (
              <tr key={ingredient.ingredientId} className="hoverable-row">
                <td>{ingredientMap[ingredient.ingredientId]}</td>
                <td>{ingredient.quantity}</td>
                <td>{measurementMap[ingredient.measurementId]}</td>
                <td>
                  <DeletePantryIngredient
                    pantryId={ingredient.pantryId}
                    token={auth.user.token}
                    loadAllIngredients={loadAllIngredients}
                    onDelete={() => {
                      loadIngredients();
                    }}
                  />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <AddPantryIngredient loadIngredients={loadIngredients} pantryIngredients={pantryIngredients} loadAllIngredients={loadAllIngredients}/>

        <div>
          <Link to="/recipe-pantry">
            <button className="recipe-pantry-button-container">Click Here for Recipes Based on Your Pantry</button>
          </Link>
        </div>
      </div>
    </div>
  );
};


export default Pantry; 