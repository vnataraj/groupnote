<?php

include 'database.php';
include 'error.php';

function getUserFromToken($token) //returns user ID based on the token
	{
		//if(validate($token)==-1){
		//	error_match("USER_NOT_FOUND");
		//	exit;
		//}
    
    global $mysqli;
    
		$result = $mysqli->query("SELECT user_id FROM users WHERE token='$token'"); 
		if($result)
			{
        $row = $result->fetch_assoc();
        return $row['user_id'];
			} 
			else
			{
				error_match("USER_NOT_FOUND");
        return -1;
			}
	}
  
function getTokenFromUser($username, $password)
{
    global $mysqli;
    
		$result = $mysqli->query("SELECT token FROM users WHERE BINARY username='$username' AND BINARY password='$password'"); 
		if($result)
			{
        $row = $result->fetch_assoc();
        if($row['token']) {
          return $row['token'];
        }
        else {
          error_match("LOGIN_ERROR");
          return -1;
        }
			} 
    else
			{
				error_match("UKNOWN_ERROR");
        return -1;
			}
}
?>