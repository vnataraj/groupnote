<?php
  include 'database.php';
  include 'error.php';
  
  $user_name = $_GET["username"];
  $password = $_GET["password"];
  $tokenator = $user_name . $password . "666";
  $token = md5($tokenator);
  
  $result = $mysqli->query("INSERT INTO users (username,password,token) VALUES('$user_name','$password','$token')");  
  
  if($result==false) {
    error_match("NEW_USER_FAILURE");
  }
  else {
    echo "Success!";
  }
  
?>