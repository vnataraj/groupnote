<?php
/*
joinSession - Adds a user to a session
Params: Session number, password
Returns: SUCCESS - User joined successfully
         FAILURE - User was not able to be added
*/

include 'getUser.php';
  
$token = $_GET["token"];
$session_id = $_GET["session_id"];
$passcode = $_GET["passcode"];

$user_id = getUserFromToken($token);

//query for session password
$query = "SELECT passcode FROM sessions WHERE session_id='$session_id'";

$result = $mysqli->query($query);

if($result) {
  $row = $result->fetch_assoc();
  //create the passcode hash, but we'll use plaintext for now
  if($row['passcode']!=NULL) {
    if(strcmp($passcode,$row['passcode'])!=0) {
      echo FAILURE;
      //error_match("SESSION_INVALID_PASSWORD");
      exit;
    }
  }
  //enroll user in session
  $query = "INSERT INTO in_session (user, session) VALUES('$user_id','$session_id')";
  
  $result = $mysqli->query($query);
  if(!$result) {
    echo "-1";
    //error_match("UNKNOWN_ERROR");
    exit;
  }
  else {
    echo SUCCESS;
  }
}

?>