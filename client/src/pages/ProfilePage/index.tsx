import {
  Avatar, Card, Col, Descriptions, Row, Switch,
} from 'antd';

import { useParams } from 'react-router-dom';
import { ReactNode, useEffect, useState } from 'react';
import moment from 'moment';
import BgProfile from '../../assets/bg-signup.jpg';
import profilavatar from '../../assets/profile_avatar.png';
import { useUser } from '../../context/UserProvider';
import UsersService from '../../services/UsersService';
import useNotification from '../../hooks/useNotification';

const ProfilePage = () => {
  const [{
    email,
    fullName,
    createdAt,
    username,
    verified,
  }, setSpecificData] = useState<Record<string, any>>({});
  const apiData = useUser();
  const { userData } = apiData;
  const { ID } = useParams();

  useEffect(() => {
    if (ID) {
      UsersService
        .getSpecificUser(ID)
        .then((userResult) => {
          console.log(userResult);
          setSpecificData(userResult);
        });
    }
  }, [ID, apiData]);

  // if (typeof userData !== 'object') {
  //   useNotification({
  //     placement: 'topRight',
  //     message: 'Error',
  //     description: 'User data is not available please logout and login again',
  //   });
  // }

  const descriptions: Record<string, ReactNode> = {
    Email: email,
    Username: username,
    Created: createdAt,
    Verified: verified,
  };

  return (
    <>
      <div
        className="profile-nav-bg"
        style={{ backgroundImage: `url(${BgProfile})` }}
      />

      <Card
        className="card-profile-head"
        bodyStyle={{ display: 'none' }}
        title={(
          <Row justify="space-between" align="middle" gutter={[24, 0]}>
            <Col span={24} md={12} className="col-info">
              <Avatar.Group>
                <Avatar size={74} shape="square" src={profilavatar} />

                <div className="avatar-info">
                  <h4 className="font-semibold m-0">{username}</h4>
                  <p>{email}</p>
                </div>
              </Avatar.Group>
            </Col>
          </Row>
            )}
      />

      <Row gutter={[24, 0]}>
        <Col span={24} md={12} className="mb-24 ">
          <Card
            bordered={false}
            className="header-solid h-full"
            title={<h6 className="font-semibold m-0">Notification Settings</h6>}
          >
            <ul className="list settings-list">
              <li>
                <h6 className="list-header text-sm text-muted">ACCOUNT</h6>
              </li>
              <li>
                <Switch defaultChecked />
                <span>Email me when someone changes product</span>
              </li>
              <li>
                <Switch />
                <span>Email me when someone adds product</span>
              </li>
              <li>
                <Switch defaultChecked />
                <span>Email me when someone deletes product</span>
              </li>
              <li>
                <h6 className="list-header text-sm text-muted m-0">
                  ANALYTICS
                </h6>
              </li>
              <li>
                <Switch defaultChecked />
                <span>Track when I logged in</span>
              </li>
              <li>
                <Switch defaultChecked />
                <span>Track each product updates</span>
              </li>
              <li>
                <Switch defaultChecked />
                <span>Users count monthly update</span>
              </li>
            </ul>
          </Card>
        </Col>
        <Col span={24} md={12} className="mb-24">
          <Card
            bordered={false}
            title={<h6 className="font-semibold m-0">Profile Information</h6>}
            className="header-solid h-full card-profile-information"
            // extra={<EditModal data={descriptions} id={ID} />}
            bodyStyle={{ paddingTop: 0, paddingBottom: 16 }}
          >
            <hr className="my-25" />
            <Descriptions title={fullName || userData?.fullName}>
              {
                Object.keys(descriptions).map((title: string, index: number) => {
                  if (!descriptions[title]) return;
                  if (title === 'Created') {
                    return (
                      <Descriptions.Item key={title} span={3} label={title}>
                        {/* @ts-ignore */}
                        {moment(descriptions[title]).format('DD/MM/YYYY')}
                      </Descriptions.Item>
                    );
                  }
                  return (
                    <Descriptions.Item label={title} span={3} key={title}>
                      {Object.values(descriptions)[index]}
                    </Descriptions.Item>
                  );
                })
              }
            </Descriptions>
          </Card>
        </Col>
      </Row>
    </>
  );
};

export default ProfilePage;
