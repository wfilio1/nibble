import { useContext, useEffect, useState } from "react";
import { Button, Card, Col, Row } from "react-bootstrap";
import { Link } from "react-router-dom";
import AuthContext from "../../context/AuthContext";

const RecipeCards = () => {
  
    const [recipeCards, setRecipeCards] = useState([]);

    const loadRecipeCards = () => {
      fetch("http://localhost:8080/api/recipes")
        .then(response => response.json())
        .then(recipe =>  {
            setRecipeCards(recipe)
        })}


  
    useEffect(loadRecipeCards, []); 


    return (
        <>
        {recipeCards !== 0 ? 
        <div>
        <div className="recipepantry-containerv2">
            <div>
                <h2 className="recipepantry-headerv2">Recipes</h2>
                </div>
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

                        <Link to={`/recipes/${recipe.recipeId}`}><button className="recipepantrybutton">View Recipe</button></Link>
                        
                        </Card.Body>
                    </Card>
                    </Col>
                ))}
                </Row>
            </div>
                </div>
        : <div className="recipepantrybody">
                No recipes found.<br />
                Do you want to add a recipe?
            </div> }
        
        </>

        );
}

export default RecipeCards;