import { useContext, useEffect, useState } from "react";
import AuthContext from "../../context/AuthContext";
import { Card, Col, Row } from "react-bootstrap";
import { Link } from "react-router-dom";

const LikedRecipes = () => {

    // api/liked/personal will return a list of likedRecipes 
    //LikedRecipes has recipeId, userId --only logged in users can access it
    // get request for the personal /api/liked/personal

    //then use Recipe -- findAll to list all the recipes BUT filter it based on what
    //recipeId is on the user's personal list 
    //once filtered we can display the list in cards

    const auth = useContext(AuthContext);

    const [likedRecipes, setLikedRecipes] = useState([]);
    const [allRecipes, setAllRecipes] = useState([]);
    const [filteredRecipes, setFilteredRecipes] = useState([]);
    const [finishedLoading, setFinishedLoading] = useState(false);

    useEffect(() => {
        const url = `${process.env.REACT_APP_EB_BACKEND}/api/liked/personal`;
        // Load liked recipes first
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

        // Then load all recipes
        fetch(`${process.env.REACT_APP_EB_BACKEND}/api/recipes`)
        .then((response) => response.json())
        .then((recipe) => {
            setAllRecipes(recipe);
        });
    }, [auth.user.token]);

    // Filter recipes when both likedRecipes and allRecipes are available
    useEffect(() => {
        const filtered = allRecipes.filter((recipe) =>
        likedRecipes.some((likedRecipe) => likedRecipe.recipeId === recipe.recipeId)
        );
        setFilteredRecipes(filtered);
        setFinishedLoading(true);
    }, [likedRecipes, allRecipes]);


    return(
        <>
        {filteredRecipes.length !== 0 && finishedLoading ?
        <div>
        <div className="recipepantry-containerv2">
            <div>
                <h2 className="recipepantry-headerv2">Liked Recipes</h2>
                </div>
                <Row xs={1} md={3} className="g-4 container_flex">
                {filteredRecipes.map((recipe, idx) => (
                    <Col key={idx}>
                    <Card className="cardbox">
                        <Card.Img variant="top" src={recipe.image} />
                        <Card.Body>
                        <Card.Title className="recipepantrybody">{recipe.title}</Card.Title>
                        <Card.Text className="recipepantrybody">
                            By: {recipe.username}
                        </Card.Text>

                        <Link to={`/recipes/${recipe.recipeId}`}><button className="recipepantrybutton">View Recipe</button></Link>
                        
                        </Card.Body>
                    </Card>
                    </Col>
                ))}
                </Row>
            </div>
                </div>
        :
            <div className="recipepantry-container">
                <h2 className="recipepantry-header">Liked Recipes</h2>
                <p className="recipepantrybody">
                    You have not added any recipes to your favorites yet.
                </p>
                <p className="recipepantrybody">
                    <Link to="/recipes">Click here to find recipes.</Link>
                </p>
            </div>
        }
        
        </>



    );
}

export default LikedRecipes;