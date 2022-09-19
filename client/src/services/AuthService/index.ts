import { postApiData } from '../../utils/api';
import { signIn, signUp } from '../../constants/endpoints';

interface AuthServiceType {
    email: string;
    password: string;
}

const AuthService = {
  async postSignIn({ email, password }: AuthServiceType) {
    if (!password) return;
    return await postApiData(
      signIn,
      {
        email,
        password,
      },
      '',
    );
  },
  async postSignUp({ email, password }: AuthServiceType) {
    if (!password) return;
    return await postApiData(
      signUp,
      {
        email,
        password,
      },
      '',
    );
  },
};

export default AuthService;
