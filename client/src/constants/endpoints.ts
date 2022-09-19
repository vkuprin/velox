const userNamespace = 'user';

const users = `${userNamespace}`;
const userAll = `${userNamespace}/all`;

const authNamespace = 'auth';

const signIn = `${authNamespace}/signin`;
const signUp = `${authNamespace}/signup`;
const restorePass = `${authNamespace}/restore_pass`;
const regreshToken = `${authNamespace}/refresh_token`;
const emailVerify = `${authNamespace}/email/verify`;
const logout = `${authNamespace}/logout`;

export {
  users,
  userAll,
  signIn,
  signUp,
  restorePass,
  regreshToken,
  emailVerify,
  logout,
};
