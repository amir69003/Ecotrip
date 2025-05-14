import axios from 'axios';
import { pathLocal, path } from './config.js'; // importe ici

let token = null;

describe("API /auth/login", () => {
  it("should login the user and return an access token", async () => {
    try {
      const response = await axios.post(`${pathLocal}api/auth/login`, {
        email: "jasmine@test.com",
        username: "jasmine",
        password: "jasmine"
      });

      expect(response.status).toBe(200);
      expect(response.data).toBeDefined();
      expect(response.data.accessToken).toBeDefined();

      token = response.data.accessToken;
      console.log("Access Token:", token);
    } catch (error) {
      fail("Request failed: " + error.message);
    }
  });
});

export { token };
