import { useState } from 'react';
import axios from "axios";

function TestApi() {
  const [leagues, setLeagues] = useState(null);
  const [user, setUser] = useState(null);
  const [token, setToken] = useState('');
  const [leagueId, setLeagueId] = useState('NA');


  const fetchLeagues = async () => {
      console.log('bearer token: ', token);
      await axios.get("http://localhost:8080/api/league/all", {
        headers:{
          'Authorization':`Bearer ${token}`
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
        'Authorization' : `Bearer ${token}`
      }
    }).then((response) => {
      console.log(response.data);
    }).catch(e => console.log(e));
  }

  const fetchLeagueById = async () => {
    console.log(`league id: ${leagueId}`)
    await axios.get("http://localhost:8080/api/league/find-by-id?leagueId=" + leagueId, {
      headers:{
        'Authorization':`Bearer ${token}`
      }
    }).then((response) => {
      console.log(response.data)
      setLeagues([response.data]);
    }).catch(e => console.log(e));
  }

  const signin = async (username, password) => {
    await axios.post("http://localhost:8080/api/auth/signin", {
      username: username,
      password: password
    }).then((response) => {
      console.log(response)
      setUser(response.data);
      setToken(response.data.accessToken);
    }).catch((error) => {
      console.log('Error signing in: ', error);
    })
  }

  return (
    <>
      <div>{JSON.stringify(leagues ? leagues : "no leagues")}</div>
      <div>Access token: {JSON.stringify(user?.accessToken)}</div>
      <button onClick={()=> fetchLeagues()}>Update Leagues</button>
      <button onClick={() => fetchLeagueById()}>Fetch league by id {leagueId}</button>
      <button onClick={() => signin('josh', '123456')}>Signin</button>
      <button onClick={() => fetchGameDataFromEspn()}>Log espn data</button>
    </>
    
  )
}

export default TestApi