<?php

include 'database.php';
  
$token = $_GET["token"];

$user_id = validate($token);
if($user_id==-1) {
  $error = "NOT_LOGGED_IN";
  include 'error.php';
  exit;
}

if ($_GET["saved"]=="yes") { //get user's sessions
  $query = "SELECT session_id, name, owner, username FROM sessions JOIN users ON owner=user_id WHERE session_id IN (SELECT session FROM in_session WHERE user=$user_id)";
}
else { //get all sessions
  $query = "SELECT session_id, name, owner, username FROM sessions JOIN users ON owner=user_id";
}

$result = $mysqli->query($query);

if($result) {
  while($row = $result->fetch_assoc()) {
    printf("%i,%s,%i,%s\n",$row['session_id'],$row['name'],$row['owner'],$row['username']);
  }
}
else {
  $error = "UNKNOWN_ERROR";
  include 'error.php'
  exit;
  
?>