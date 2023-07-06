import {createContext, useState} from 'react';
import {useMutation} from 'react-query';
import axios from 'axios';
import PropTypes from 'prop-types';

export const UserContext = createContext();

export const UserProvider = ({children}) => {

    const [user, setUser] = useState(() => JSON.parse(sessionStorage.getItem('user')));
    const [token, setToken] = useState(user?.accessToken);

    const signinMutation = useMutation(({username, password}) => axios
        .post('http://localhost:8080/api/auth/signin', {username, password}), {
        onSuccess: (data) => {
            setUser(data.data);
            setToken(data.data.accessToken);
            sessionStorage.setItem('user', JSON.stringify(data.data));
            console.log('User signed in successfully: ', data);
            return data.data;
        },
        onError: (error) => {
            throw new Error(error.response?.data);
        }
    })

    const signupMutation = useMutation(({username, email, password}) => {
        const credentials = {username, password};

        return axios.post('http://localhost:8080/api/auth/signup', {username, email, password})
            .then((data) => {
                return {...data, credentials};  // Return data and credentials together.
            }).catch((error) => {
                throw new Error(error.response?.data);
            });
    }, {
        onSuccess: async (data) => {
            console.log('User created: ', data.data);
            try {
                await signIn(data.credentials.username, data.credentials.password);
            } catch (error) {
                console.log('Error signing in after signup: ', error);
            }
        },
        onError: (error) => {
            throw new Error(error.response?.data)
        },
    });


    const signIn = (username, password) => {
        return signinMutation.mutateAsync({username, password});
    }

    const signUp = (username, email, password) => {
        return signupMutation.mutateAsync({username, email, password});
    }

    const signOut = () => {
        sessionStorage.setItem('user', null);
        setUser(null);
        setToken(null);
    }

    return (
        <UserContext.Provider value={{user, token, signIn, signOut, signUp}}>
            {children}
        </UserContext.Provider>
    );
};

UserProvider.propTypes = {
    children: PropTypes.node,
};