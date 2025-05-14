import axios from 'axios';

let token = null;
let pathLocal = "http://localhost:8080/";
let path = "http://192.168.75.123/";

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
