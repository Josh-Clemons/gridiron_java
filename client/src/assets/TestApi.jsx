import { useState, useEffect, useContext } from 'react';
import { useMutation } from 'react-query'
import axios from "axios";
import { UserContext } from '../contexts/UserContext';

function TestApi() {
  const [leagues, setLeagues] = useState(null);
  const [token, setToken] = useState('');
  const [sessionToken, setSessionToken] = useState('');
  const [leagueId, setLeagueId] = useState('NA');
  const { user, setUser } = useContext(UserContext);

  useEffect(()=>{
    const tempToken = sessionStorage.getItem('access_token');
    setSessionToken(tempToken);
    setToken(tempToken);
  }, [])


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

  const handleSignin = () => {
    signinMutation.mutate(['josh', '123456'])
  }
  const signinMutation = useMutation(([username, password]) => axios
    .post('http://localhost:8080/api/auth/signin', { username, password }), {
    onSuccess: (data) => {
      setUser(data.data);
      setToken(data.data.accessToken);
      sessionStorage.setItem('access_token', data.data.accessToken);
      console.log('User signed in successfully: ', data);
    },
    onError: (error) => {
      console.log('Error signing in: ', error)
    }
  })

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

  const getSessionToken = () => {
    setSessionToken(sessionStorage.getItem('access_token'));
    console.log(sessionStorage.getItem('access_token'));
  }

  return (
    <>
      <div>{JSON.stringify(leagues ? leagues : "no leagues")}</div>
      <div>Access token: {token}</div>
      <div>Session token: {sessionToken}</div>
      <div>{JSON.stringify(user)}</div>
      <button onClick={()=> fetchLeagues()}>Update Leagues</button>
      <button onClick={() => fetchLeagueById()}>Fetch league by id {leagueId}</button>
      <button onClick={() => signin('josh', '123456')}>Signin - old </button>
      <button onClick={handleSignin}>Signin - react query</button>
      <button onClick={() => fetchGameDataFromEspn()}>Log espn data</button>
      <button onClick={() => getSessionToken()}>Set session token</button>
    </>
    
  )
}

export default TestApi