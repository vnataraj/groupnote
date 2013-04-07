<?php
  include 'database.php';
  include 'error.php';
  
  $user_name = $_GET["username"];
  $password = $_GET["password"];
  $tokenator = $user_name . $password . "666";
  $token = md5($tokenator);
  
  $result = $mysqli->query("INSERT INTO users (username,password,token) VALUES('$user_name','$password','$token')");  
  
  if($result==false) {
    echo FAILURE;
   // error_match("NEW_USER_FAILURE");
  }
  else {
    $new_user_id = $mysqli->insert_id;
    $query = "SELECT token FROM users WHERE user_id='$new_user_id'";
    $result = $mysqli->query($query);
    if ($result==false) {
      echo FAILURE;
    }
    else {
      $row = $result->fetch_assoc();
      if($row['token']) {
        echo $row['token'];
      }
      else {
        echo FAILURE;
      }
    }
  }
  
?>