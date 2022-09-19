import { SetStateAction, useState } from 'react';
import {
  Form,
  Tag,
} from 'antd';
import { Link } from 'react-router-dom';
import moment from 'moment';
import UsersService from '../../services/UsersService';

import TableContainer from '../../containers/TableContainer';
import OperatorsContainer from '../../containers/OperatorsContainer';
import useCombineTable from '../../hooks/tableHooks/useCombineTable';

const UsersPage = () => {
  const [userData, setUserData] = useState([]);
  const [editingKey, setEditingKey] = useState('');
  const [form] = Form.useForm();

  const { handleAdd, handleCreate, handleDelete } = useCombineTable({
    form,
    setUserData,
    userData,
    setEditingKey,
    GetService: UsersService.getUsers,
    AddService: UsersService.createUser,
    UpdateService: UsersService.updateUser,
    DeleteService: UsersService.deleteUser,
  });

  const isEditing = (record: { id: string; }) => record.id === editingKey;

  const cancel = () => {
    setEditingKey('');
  };

  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      width: '5%',
      defaultSortOrder: 'descend',
      render: (text: string, record: {id: string}) => <Link to={`/profile/${record.id}`}>{text}</Link>,
      sorter: (aLower: number, bLower: number) => {
        if (aLower > bLower) {
          return 1;
        }
        return -1;
      },
    },
    {
      title: 'Username',
      dataIndex: 'username',
      width: '20%',
      editable: true,
      render: (text: string, record: {id: string}) => <Link to={`/profile/${record.id}`}>{text}</Link>,
      sorter: (aLower: number, bLower: number) => {
        if (aLower > bLower) {
          return 1;
        }
        return -1;
      },
    },
    {
      title: 'Created Date',
      dataIndex: 'createdAt',
      width: '20%',
      render: (text: string, record: {id: string}) => moment(text).format('DD/MM/YYYY'),
    },
    {
      title: 'User Verified',
      dataIndex: 'verified',
      width: '10%',
      editable: false,
      render: (tags: string) => (
        tags ? (
          <Tag color="green">Active</Tag>
        ) : (
          <Tag color="red">Inactive</Tag>
        )
      ),
    },
    {
      title: 'Email Verified',
      dataIndex: 'emailVerified',
      width: '10%',
      // editable: true,
      render: (tags: string) => (
        tags ? (
          <Tag color="green">Active</Tag>
        ) : (
          <Tag color="red">Inactive</Tag>
        )
      ),
      sorter: (a: { fullName: string; }, b: { fullName: string; }) => {
        const aLower = a.fullName.split(' ')[0].toLowerCase();
        const bLower = b.fullName.split(' ')[0].toLowerCase();
        if (aLower > bLower) {
          return 1;
        }
        return -1;
      },
    },
    {
      title: 'Country',
      dataIndex: 'country',
      width: '20%',
      // editable: true,
      sorter: (a: { phoneNumber: number; }, b: { phoneNumber: number; }) =>
        a.phoneNumber - b.phoneNumber,
    },
    {
      title: 'First Name',
      dataIndex: 'firstName',
      width: '5%',
      // editable: true,
      render: (text: string, record: {id: string}) => <Link to={`/profile/${record.id}`}>{text}</Link>,
      sorter: (a: { namePrefix: string; }, b: { namePrefix: string; }) => {
        const aLower = a.namePrefix.split(' ')[0].toLowerCase();
        const bLower = b.namePrefix.split(' ')[0].toLowerCase();
        if (aLower > bLower) {
          return 1;
        }
        return -1;
      },
    },
    {
      title: 'Last Name',
      dataIndex: 'lastName',
      width: '5%',
      // editable: true,
      render: (text: string, record: {id: string}) => <Link to={`/profile/${record.id}`}>{text}</Link>,
      sorter: (a: { namePrefix: string; }, b: { namePrefix: string; }) => {
        const aLower = a.namePrefix.split(' ')[0].toLowerCase();
        const bLower = b.namePrefix.split(' ')[0].toLowerCase();
        if (aLower > bLower) {
          return 1;
        }
        return -1;
      },
    },
    {
      title: 'Date of Birth',
      dataIndex: 'dob',
      width: '10%',
      // editable: true,
      sorter: (a: { fullName: string; }, b: { fullName: string; }) => {
        const aLower = a.fullName.split(' ')[0].toLowerCase();
        const bLower = b.fullName.split(' ')[0].toLowerCase();
        if (aLower > bLower) {
          return 1;
        }
        return -1;
      },
    },
    {
      title: 'operation',
      dataIndex: 'operation',
      render: (_: unknown, record: { id: string; }) => {
        const editable = isEditing(record);
        return (
          <OperatorsContainer
            save={handleCreate}
            cancel={cancel}
            edit={edit}
            record={record}
            editingKey={editingKey}
            handleDelete={handleDelete}
            editable={editable}
          />
        );
      },
    },
  ];

  const editFields = {
    id: '',
    domainName: '',
    active: false,
    parentProviderId: '',
    sentence: '',
  };

  const edit = (record: { id: SetStateAction<string>; }) => {
    form.setFieldsValue({
      ...editFields,
      ...record,
    });
    setEditingKey(record.id);
  };

  return (
    <TableContainer
      title="Users"
      dataFetch={userData}
      columns={columns}
      handleAdd={handleAdd}
      form={form}
      isEditing={isEditing}
      setEditingKey={setEditingKey}
      layoutData={[]}
    />
  );
};

export default UsersPage;
