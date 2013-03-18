<?php

  include 'database.php';
  
  $user_name = $_GET["user_name"];
  $password = $_GET["password"];
  
  $token = md5($user_name+$password+"666");
  
  $result = $mysqli->query("INSERT INTO users (username,password,token) VALUES('$user_name','$password','$token')");  
  
  if($result==false) {
    $error = "NEW_USER_FAILURE";
    include 'error.php';
  }
  
?>