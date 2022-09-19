import {
  getApiData,
  postApiData,
  putApiData,
  deleteApiData,
} from '../../utils/api';
import { signUp, userAll, users as usersURI } from '../../constants/endpoints';

const UsersService = {
  async getUsers() {
    return getApiData(
      userAll,
      '',
    );
  },
  async updateUser(id: string | undefined, body: any) {
    return putApiData(
      `${usersURI}${id}`,
      body,
      '',
    );
  },
  async getSpecificUser(id: string | undefined) {
    return getApiData(
      `${usersURI}/${id}`,
      '',
    );
  },
  async createUser(body: object) {
    return postApiData(
      signUp,
      body,
      '',
    );
  },
  async deleteUser(id: string) {
    return deleteApiData(
      `${usersURI}/${id}`,
      '',
    );
  },
};

export default UsersService;
