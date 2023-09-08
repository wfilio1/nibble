import { useContext, useEffect, useState } from "react";
import { Button, Image, Table } from "react-bootstrap"
import { Link, useParams } from "react-router-dom";
import AuthContext from "../../context/AuthContext";
import Comments from "./Comments";
import AddLikedRecipes from "./AddLikedRecipe";

const Recipe = () => {
    const params = useParams();
    const auth = useContext(AuthContext);
    const [recipe, setRecipe] = useState(null);
    const [recipeIngredients, setRecipeIngredients] = useState([]);
    const [ingredients, setIngredients] = useState([]);
    const [measurements, setMeasurements] = useState([]);
    const [pantryItems, setPantryItems] = useState([]);
    const [likedRecipes, setLikedRecipes] = useState(null);
    const [liked, setLiked] = useState(false);
    const [likedRecipeId, setLikedRecipeId] = useState(null);



//Get all ingredients in order to display the ingredient name
    const loadIngredients = (recipeId) => {
        fetch(`${process.env.REACT_APP_EB_BACKEND}/api/ingredients/${recipeId}`)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to fetch recipe ingredients');
            }
        })
        .then(recipeIngredients => {
            fetch(`${process.env.REACT_APP_EB_BACKEND}/api/ingredients/`)
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Failed to fetch ingredients');
                }
            })
            .then(allIngredients => {
                const filteredIngredients = allIngredients.filter(ingredient => {
                    return recipeIngredients.some(ri => ri.ingredientId === ingredient.ingredientId);
                });
                setRecipeIngredients(recipeIngredients);
                setIngredients(filteredIngredients);
            })
            .catch(error => {
                console.error(error);
            });
            
            fetch(`${process.env.REACT_APP_EB_BACKEND}/api/measurements`)
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Failed to fetch measurements');
                }
            })
            .then(allMeasurements => {
                const filteredMeasurements = allMeasurements.filter(m => {
                    return recipeIngredients.some(ri => ri.measurementId === m.measurementId);
                });
                setMeasurements(filteredMeasurements);
            })
            .catch(error => {
                console.error(error);
            });
        
        })
        .catch(error => {
            console.error(error);
        });
    };



//Get all pantry ingredients so that we can compare if user have the ingredients
//in their pantry
    const loadPantryItems = () => {
        if(auth.user && auth.user.token) {
            const url = `${process.env.REACT_APP_EB_BACKEND}/api/pantry/personal`;

            fetch(url, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    Accept: "application/json",
                    Authorization: "Bearer " + auth.user.token,
                }
            } )
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Failed to fetch pantry ingredients');
                }
            })
            .then(allPantryItems => {
                setPantryItems(allPantryItems);
            })
        }
    }


    //Gets all personal liked recipes
    useEffect(() => {
        if(auth.user) {
            const url = `${process.env.REACT_APP_EB_BACKEND}/api/liked/personal`;

            fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
                Authorization: "Bearer " + auth.user.token,
            },
            })
            .then((response) => response.json())
            .then((data) => {
                setLikedRecipes(data);
            })
            .catch((error) => {
                console.error("Error fetching personal liked recipes:", error);
            });
        }
        
    }, [auth.user]);



//Gets the recipe based on the recipeId, as well as check if the likedRecipes
//recipeId matches its own recipeId
    useEffect(() => {
        const url = `${process.env.REACT_APP_EB_BACKEND}/api/recipes/${params.id}`;
        fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
            },
        })
        .then((response) => {
            if (response.ok) {
                response.json().then((recipe) => {
                    setRecipe(recipe);
                    if(auth.user) {
                      loadPantryItems();  
                    }
                    loadIngredients(params.id);

                    if (likedRecipes) {
                        const likedRecipe = likedRecipes.find((likedRecipe) => likedRecipe.recipeId === recipe.recipeId);
                        if (likedRecipe) {
                            setLiked(true); // Set liked to true
                            setLikedRecipeId(likedRecipe.likedRecipesId);
                        }
                    }
                    
                });
            }
        })
        .catch((error) => {
            console.error(error);
        });
    }, [params.id, likedRecipes]);

    if (recipe === null) {
        return null;
    }

    const getMeasurementName = (measurementId) => {
        const measurement = measurements.find(m => m.measurementId === measurementId);
        if (measurement) {
            return measurement.measurementName;
        }
    }

    const checkPantryQuantity = (ingId, quantity, measurement) => {
        if(pantryItems.some(p => p.ingredientId === ingId && p.quantity >= quantity && p.measurementId === measurement)) {
            return true;
        }
        else {
            return false;
        }
    }


//check if the recipe was created by the logged in user
    const checkUser = (user) => {
        if(auth.user && auth.user.username === user) {
            return true;
        }
        else {
            return false;
        }
    }


    return (
        <section id={recipe.recipeId}>
            <div className="recipeviewcontainer">
                <div className="recipeviewheader">
                    <h2 className="recipetitle">{recipe.title}</h2>
                    <h6>posted by: {recipe.username}</h6> {auth.user && <AddLikedRecipes recipeId={recipe.recipeId} setLiked={setLiked} liked={liked} setLikedRecipeId={setLikedRecipeId} likedRecipeId={likedRecipeId} />}
                </div>
    
                <div className="recipeHeader-pic">
                    <Image src={recipe.image} alt={recipe.image} roundedCircle />
                </div>
    
                <Table borderless>
                <tbody>
                    <tr className="recipecookingcenter">
                        <td className="recipecookingtime" colSpan={4}>
                            <b>Estimated Cooking Time: {recipe.cookTimeMin} mins.</b>
                        </td>
                    </tr>

                    <tr>
                        <td className="recipeingredients">
                            <h5 className="recipeviewtitle">INGREDIENTS</h5>
                            <ul>
                                {ingredients.map(i => (
                                    <li key={i.ingredientId}>
                                        {i.ingredientName}{' '}
                                        {recipeIngredients.map(ri => {
                                            if (ri.ingredientId === i.ingredientId && recipe.recipeId === ri.recipeId) {
                                                return (
                                                    <>
                                                        {ri.quantity} {getMeasurementName(ri.measurementId)}
                                                        {checkPantryQuantity(i.ingredientId, ri.quantity, ri.measurementId) ? ' ✔️' : ' ❌'}
                                                    </>
                                                );
                                            }
                                            return null;
                                        })}
                                    </li>
                                ))}
                            </ul>
                        </td>
                        <td className="recipedirections" colSpan={2} style={{ whiteSpace: 'pre-wrap', overflowWrap: 'break-word' }}>
                            <h5 className="recipeviewtitle">DIRECTIONS</h5>
                            <div style={{ whiteSpace: 'pre-wrap', overflowWrap: 'break-word' }}>
                            {recipe.steps}
                            </div>
                        </td>
                        <td className="recipeviewdelete" >
                            {checkUser(recipe.username) ? <Link to={`/delete/${recipe.recipeId}`} style={{ color: 'white', textDecoration: 'none' }}>Delete Recipe</Link> : null}
                        </td>
                    </tr>
                </tbody>
            </Table>
        </div>
        
        <Comments recipeId={parseInt(params.id)} />
    </section>
);
    
}

export default Recipe;