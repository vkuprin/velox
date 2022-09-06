import {
  useState, useEffect, ReactNode, SetStateAction,
} from 'react';
import { useLocation } from 'react-router-dom';
import { Layout, Drawer } from 'antd';
import Menu from '../../components/Menu';
import Header from '../../components/Header';

const { Header: AntHeader, Content, Sider } = Layout;

interface MainProps {
  children?: ReactNode;
}

const Main = ({ children }: MainProps) => {
  const [visible, setVisible] = useState(false);
  const [sidenavType, setSidenavType] = useState('white');

  const openDrawer = () => setVisible(!visible);
  const handleSidenavType = (type: SetStateAction<string>) => setSidenavType(type);

  let { pathname } = useLocation();
  pathname = pathname.replace('/', '');

  return (
    <Layout
      className={`layout-dashboard ${
        pathname === 'profile' ? 'layout-profile' : ''
      }`}
    >
      <Drawer
        title={false}
        closable={false}
        onClose={() => setVisible(false)}
        visible={visible}
        width={250}
        className="drawer-sidebar"
      >
        <Layout className="layout-dashboard">
          <Sider
            trigger={null}
            width={250}
            theme="light"
            className={`sider-primary ant-layout-sider-primary ${
              sidenavType === '#fff' ? 'active-route' : ''
            }`}
            style={{ background: sidenavType }}
          >
            <Menu color="white" />
          </Sider>
        </Layout>
      </Drawer>
      <Sider
        breakpoint="lg"
        collapsedWidth="0"
        onCollapse={(collapsed, type) => {
          console.log(collapsed, type);
        }}
        trigger={null}
        width={250}
        theme="light"
        className={`sider-primary ant-layout-sider-primary ${
          sidenavType === '#fff' ? 'active-route' : ''
        }`}
        style={{ background: sidenavType }}
      >
        <Menu color="white" />
      </Sider>
      <Layout>
        <AntHeader>
          <Header
            onPress={openDrawer}
            name={pathname}
            subName={pathname}
            handleSidenavType={handleSidenavType}
          />
        </AntHeader>
        <Content className="content-ant">{children}</Content>
      </Layout>
    </Layout>
  );
};

export default Main;
