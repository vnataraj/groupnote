<?php
	include 'getUser.php';
	
	$token = $_GET["token"];
  getUserFromToken($token);
	  
  $note_id = intval($_GET["note_id"]); 
  
  $result = $mysqli->query("SELECT content,username FROM notes JOIN users ON notes.owner=users.user_id WHERE note_id=$note_id");

  if($result) {
    while($row = $result->fetch_assoc()) {
      $content = $row['content'];
      // $username = $row['username'];
      printf("$content");
    }
  } 
?>