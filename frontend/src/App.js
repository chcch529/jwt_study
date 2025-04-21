import { BrowserRouter, Route , Routes } from "react-router-dom";
import Home from './Home';
import {LoginPage} from './LoginPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/">
          <Route path="" element={<Home /> }/>
          <Route path="login" element={<LoginPage /> }/>
        </Route>

      </Routes>

    </BrowserRouter>
  );
}

export default App;
