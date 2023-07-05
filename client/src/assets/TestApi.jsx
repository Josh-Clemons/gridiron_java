import { useState, useContext } from 'react';
import axios from "axios";
import { UserContext } from '../contexts/UserContext';

function TestApi() {

  const { user, token, signIn, signOut } = useContext(UserContext);

  const [leagues, setLeagues] = useState(null);
  const [leagueId, setLeagueId] = useState('NA');



  const fetchLeagues = async () => {
    console.log('bearer token: ', token);
    await axios.get("http://localhost:8080/api/league/all", {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    }).then((response) => {
      let randomIndex = Math.floor(Math.random() * response.data.length);
      console.log('trying to get league Id: ', response.data[randomIndex].id)
      setLeagueId(response.data[randomIndex].id);
      setLeagues(response.data);
    }).catch((error) => {
      console.log("Error fetching leagues: ", error);
    });
  }

  const fetchGameDataFromEspn = async () => {
    console.log('fetching game data......')
    await axios.get("http://localhost:8080/api/gamedata/espn", {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    }).then((response) => {
      console.log(response.data);
    }).catch(e => console.log(e));
  }

  const fetchLeagueById = async () => {
    console.log(`league id: ${leagueId}`)
    await axios.get("http://localhost:8080/api/league/find-by-id?leagueId=" + leagueId, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    }).then((response) => {
      console.log(response.data)
      setLeagues([response.data]);
    }).catch(e => console.log(e));
  }

  const handleSignin = () => {
    signIn('josh', '123456')
  }

  return (
    <>
      <div>{JSON.stringify(leagues ? leagues : "no leagues")}</div>
      <div>Username: {user?.username}</div>
      <div>Session token: {user?.accessToken}</div>
      <div>Token variable: {token}</div>
      <div>{JSON.stringify(user)}</div>
      <button onClick={() => fetchLeagues()}>Update Leagues</button>
      <button onClick={() => fetchLeagueById()}>Fetch league by id {leagueId}</button>
      <button onClick={(handleSignin)}>Signin - react query</button>
      <button onClick={signOut}>Signout</button>
      <button onClick={() => fetchGameDataFromEspn()}>Log espn data</button>
    </>

  )
}

export default TestApi