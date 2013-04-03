<?php
/*
newSession - Creates a new session
Params: Session Name, password (can be blank), owner
Returns: success (owner+name unique)
         error (owner+name not unique)
           */
include 'getUser.php';
  
$token = $_GET["token"];
$user_id = getUserFromToken($token);
  
$session_name = $_GET["session_name"];
$passcode = $_GET["passcode"];

if(strcmp($passcode,"")==0) {
  //echo "passcode null...inserting without<br>";
  $query = "INSERT INTO sessions (name) VALUES('$session_name')";
}
else {
  //echo "passcode was not null...inserting with<br>";
  $query = "INSERT INTO sessions (name, passcode) VALUES('$session_name','$passcode')";
}

//echo "query is $query";
$result = $mysqli->query($query);  

if($result==false) {
  echo "0";
  //error_match("CREATE_SESSION_FAILURE");
  exit;
}
else {
  $query = "INSERT INTO in_session (user, session, is_owner) VALUES ('$user_id','$mysqli->insert_id','1')";
  $result = $mysqli->query($query);  

  if($result==false) {
    //error_match("ENROLL_USER_FAILURE");
    echo "0";
    exit;
  }
}
echo "1";
  
?>