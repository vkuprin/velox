import React from 'react';
import {
  Layout,
  Button,
  Row,
  Col,
  Typography,
  Form,
  Input,
} from 'antd';
import cs from 'classnames';
import { useNavigate } from 'react-router-dom';
import styles from './index.module.scss';

const { Title } = Typography;
const { Content } = Layout;

interface LoginProps {
  username: string;
  password: string;
}

const SignIn = () => {
  const navigate = useNavigate();

  const onFinish = ({ password, username }: LoginProps) => {
    if (username === 'venox' && password === '12345678') {
      navigate('/home');
    }
  };

  const onFinishFailed = (errorInfo: Error | any) => {
    console.error('Failed:', errorInfo);
  };

  return (
    <Layout className={cs('layout-default', 'layout-signin', styles.zoom)}>
      <Content className="signin">
        <Row gutter={[24, 0]} justify="space-around">
          <Col>
            <Title className="mb-15">Support</Title>
            <Form
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
              layout="vertical"
              className={cs('row-col', styles.formContainer)}
            >
              <Form.Item
                className="username"
                name="username"
                rules={[
                  {
                    required: true,
                    message: 'Please input your email!',
                  },
                ]}
              >
                <Input placeholder="Email" />
              </Form.Item>

              <Form.Item
                className="username"
                name="password"
                rules={[
                  {
                    required: true,
                    message: 'Please input your password!',
                  },
                ]}
              >
                <Input placeholder="Password" />
              </Form.Item>

              <Form.Item>
                <Button
                  type="primary"
                  htmlType="submit"
                  style={{ width: '100%', marginTop: '20px' }}
                >
                  SIGN IN
                </Button>
              </Form.Item>
            </Form>
          </Col>
        </Row>
      </Content>
    </Layout>
  );
};

export default SignIn;
