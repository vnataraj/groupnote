<?php
include 'database.php';
include 'error.php';
function getUserFromToken($token)
	{
		if(validate($token)==-1){
			error_match("USER_NOT_FOUND");
			exit;
		}
		$result = $mysqli->query("SELECT username FROM users WHERE token=$token"); 
		if($result)
			{
				return $result;
			} 
			else
			{
				error_match("unknown");
			}
	}
?>