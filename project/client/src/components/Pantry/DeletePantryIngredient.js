
const DeletePantryIngredient = (props) => {
  const handleDelete = () => {
      fetch(`http://localhost:8080/api/pantry/delete/${props.pantryId}`, {
          method: "DELETE",
          headers: {
              Authorization: `Bearer ${props.token}`,
          },
      })
      .then((response) => {
          if (response.ok) {
              props.onDelete();
              props.loadAllIngredients();
            //   props.loadIngredients();
            //   window.location.reload();
          } else {
              console.error(`Unexpected response status code: ${response.status}`);
          }
      })
      .catch((error) => {
          console.error("Error deleting pantry ingredient:", error);
      });
  };



  return ( 
    <button className="delete-button" onClick={handleDelete}> 
      Delete 
    </button> 
  ); 

}; 

export default DeletePantryIngredient;