<?php

  include 'database.php';
  
  $session_id = intval($_GET["session_id"]); 

  $result = $mysqli->query("SELECT note_id,username FROM notes JOIN users ON notes.owner=users.user_id WHERE session=$session_id");

  if($result) {
    while($row = $result->fetch_assoc()) {
      $note_id = $row['note_id'];
      $username = $row['username'];
      printf("$note_id,$username\n\n");
      // printf("%i %s %s\n",$note_id,$content,$owner);
    }
  }
    // $stmt->close();
  
?>