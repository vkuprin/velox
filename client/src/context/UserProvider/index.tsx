import {
  createContext, ReactNode, useContext, useMemo,
} from 'react';
import useLocalStorage from '../../hooks/useLocalStorage';

export interface UserContextType {
   userData: any;
    setUserData: (value: Record<string, UserContextType['userData']>) => void;
}

const UserContext = createContext<UserContextType>(null!);

const UserProvider = ({ children }: { children: ReactNode }) => {
  const [userData, setUserData] = useLocalStorage('userProvider');

  const value: UserContextType = {
    userData,
    setUserData,
  };

  const memoValue = useMemo(() => value, [value]);

  return <UserContext.Provider value={memoValue}>{children}</UserContext.Provider>;
};

const useUser = () => useContext(UserContext);

export {
  UserProvider,
  useUser,
};
