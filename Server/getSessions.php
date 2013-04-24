<?php

include 'getUser.php';
  
$token = $_GET["token"];

$user_id = getUserFromToken($token);

if (strcmp($_GET["saved"],"yes")==0) { //get user's sessions
  $query = "SELECT session_id, name, latitude, longitude, radius FROM sessions WHERE session_id IN (SELECT session FROM in_session WHERE user=$user_id) ORDER BY name ASC";
}
else if (strcmp($_GET["saved"],"no")==0) { //get sessions user is NOT in
  $query = "SELECT session_id, name, latitude, longitude, radius FROM sessions WHERE session_id NOT IN (SELECT session FROM in_session WHERE user=$user_id) ORDER BY name ASC";
}
else { //get all sessions
  $query = "SELECT session_id, name, latitude, longitude, radius FROM sessions ORDER BY name ASC";
}

$result = $mysqli->query($query);

if($result) {
  while($row = $result->fetch_assoc()) {
    printf("%d,%s, %f, %f, %d;\n",$row['session_id'],$row['name'],$row['latitude'],$row['longitude'],$row['radius']);
  }
}
else {
  //error_match("UNKNOWN_ERROR");
  echo FAILURE;
  exit;
}
?>