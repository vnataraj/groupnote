<?php

include 'database.php';

$session_id = intval($_GET["session_id"]); 

$token = $_GET["token"];

$user_id = validate($token);
if($user_id==-1) {
  exit;
}



$result = $mysqli->query("SELECT note_id,username FROM notes JOIN users ON notes.owner=users.user_id WHERE session=$session_id");

if($result) {
  while($row = $result->fetch_assoc()) {
    $note_id = $row['note_id'];
    $username = $row['username'];
    printf("$note_id,$username\n\n");
    // printf("%i %s %s\n",$note_id,$content,$owner);
  }
}
  
?>