<?php

include 'getUser.php';
  
$token = $_GET["token"];

$user_id = getUserFromToken($token);

if (strcmp($_GET["saved"],"yes")==0) { //get user's sessions
  $query = "SELECT session_id, name, owner, username FROM sessions JOIN users ON owner=user_id WHERE session_id IN (SELECT session FROM in_session WHERE user=$user_id)";
}
else { //get all sessions
  $query = "SELECT session_id, name, owner, username FROM sessions JOIN users ON owner=user_id";
}

$result = $mysqli->query($query);

if($result) {
  while($row = $result->fetch_assoc()) {
    printf("%d,%s,%d,%s\n",$row['session_id'],$row['name'],$row['owner'],$row['username']);
  }
}
else {
  error_match("UNKNOWN_ERROR");
  exit;
}
?>