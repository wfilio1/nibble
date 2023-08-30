import { useContext, useEffect, useState } from "react";
import { Button, Card, Col, Row } from "react-bootstrap";
import { Link } from "react-router-dom";
import AuthContext from "../../context/AuthContext";

const RecipeCardsFromPantry = () => {
    const auth = useContext(AuthContext);
    const user = auth.user;

    const [pantryIngredients, setPantryIngredients] = useState([]);
    const [recipes, setRecipes] = useState([]);
    const [recipeCards, setRecipeCards] = useState([]);
    const [recipeIngredientsLoaded, setRecipeIngredientsLoaded] = useState(false);

    //pantryIngredients has ingredientId (ingId 26, 27)
    //now, check if those ids are in any recipeIng that belongs to the same recipe
    //if pantryIngredients.ingredientId === recipeIng.ingredientId , then matchingIngId
    //use that matchingIngId to go thru the recipeIng that has that ingId, and get the recipeId
    //only display those recipeId

    const loadPantryIngredients = () => {
        fetch("http://localhost:8080/api/pantry/personal", {
            headers: {
                Authorization: `Bearer ${auth.user.token}`,
            },
        })
        .then(response => response.json())
        .then(allPantryIngredients => {
            //this is storing the ingredientIds from personal pantry
            //now, I want to compare this ingredientId to recipeIngredient.ingredientId
            setPantryIngredients(allPantryIngredients);
        })
        .catch(error => {
            console.error("Error fetching pantry ingredients:", error);
        });
    };

    const loadRecipes = () => {
        fetch("http://localhost:8080/api/recipes")
          .then(response => response.json())
          .then(recipe =>  {
              setRecipes(recipe)
          })}

    const loadRecipeIngredients = (evt) => {
    Promise.all(
        recipes.map(recipe =>
            fetch(`http://localhost:8080/api/ingredients/${recipe.recipeId}`)
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error('Failed to fetch recipe ingredients');
                    }
                })
        )
    )
    .then(recipeIngDataArray => {

        const recipesCopy = [...recipes];

        for(let i = 0; i < recipesCopy.length; i++) {
            recipesCopy[i].ingredients = recipeIngDataArray[i];
        }

        const filteredRecipes = recipesCopy.filter(recipe => {
            return recipe.ingredients.every(recipeIng => {
                const pantryIng = pantryIngredients.find(pantryIng => pantryIng.ingredientId === recipeIng.ingredientId);
                return pantryIng && pantryIng.quantity >= recipeIng.quantity;
            });
        });

            setRecipeCards(filteredRecipes)

            if(recipeCards.length !== 0 ) {
                setRecipeIngredientsLoaded(true);
            }
            
            
            
        })
        .catch(error => {
            console.error("Error fetching recipe ingredients:", error);
        });
    };



    useEffect(() => {
        if (user) {
            loadPantryIngredients();
            loadRecipes();
        }
    }, [user]);

    useEffect(() => {
        loadRecipeIngredients();
        ifEmpty();
    }, [recipes])
    

    const ifEmpty = () => {
        if(!recipeIngredientsLoaded) {
        return (
            <div className="recipepantry-container">
            <h2 className="recipepantry-header">Recipes Based on Your Pantry</h2>
            <p className="recipepantrybody">
                    No recipes found.<br />
                    Make sure to add more ingredients to your pantry!
            </p>
            </div>)
    }

    }

    return (
        <>
            {recipeCards.length !== 0 ? (
                <div className="recipepantry-container">
                    <h2 className="recipepantry-header">Recipes Based on Your Pantry</h2>
                    <Row xs={1} md={3} className="g-4 container_flex">
                        {recipeCards.map((recipe, idx) => (
                            <Col key={idx}>
                                <Card className="cardbox">
                                    <Card.Img variant="top" src={recipe.image} />
                                    <Card.Body>
                                        <Card.Title className="recipepantrybody">{recipe.title}</Card.Title>
                                        <Card.Text className="recipepantrybody">
                                            By: {recipe.username}
                                        </Card.Text>
                                        <Link to={`/recipes/${recipe.recipeId}`}>
                                        <button className="recipepantrybutton" type="button">View Recipe</button>
                                        
                                        </Link>
                                    </Card.Body>
                                </Card>
                            </Col>
                        ))}
                    </Row>
                </div>
            ) : (
                ifEmpty()
            )}
        </>
    );
};

export default RecipeCardsFromPantry;