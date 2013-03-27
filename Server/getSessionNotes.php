<?php

include 'getUser.php';
  
$token = $_GET["token"];
$user_id = getUserFromToken($token);

$session_id = intval($_GET["session_id"]); 

//here we should check that a user is enrolled in the session
$result = $mysqli->query("SELECT note_id,username FROM notes JOIN users ON notes.owner=users.user_id WHERE session=$session_id");

if($result) {
  while($row = $result->fetch_assoc()) {
    $note_id = $row['note_id'];
    $username = $row['username'];
    printf("$note_id,$username\n\n");
    // printf("%i %s %s\n",$note_id,$content,$owner);
  }
}
else {
  error_match("UNKNOWN_ERROR");
}
  
?>