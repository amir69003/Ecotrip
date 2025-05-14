// tests/authRouter.spec.js
import axios from 'axios';
import { pathLocal, path, idLocal, id} from './config.js';

let token = null;

describe("API Authentication and User Retrieval", () => {
  it("should login the user and return an access token", async () => {
    try {
      const response = await axios.post(`${path}api/auth/login`, {
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
      fail("Login failed: " + error.message);
    }
  });

  it("should retrieve all users with a valid token", async () => {
    try {
      expect(token).not.toBeNull();

      const response = await axios.get(`${path}api/users`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      expect(response.status).toBe(200);
      expect(Array.isArray(response.data)).toBeTrue();
      console.log("Users:", response.data);
    } catch (error) {
      fail("User fetch failed: " + error.message);
    }
  });

  it("should retrieve one user with a valid token", async () => {
    try {
      expect(token).not.toBeNull();

      const response = await axios.get(`${path}api/users/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      expect(response.status).toBe(200);
      expect(typeof response.data).toBe("object");
      console.log("Users:", response.data);
    } catch (error) {
      fail("User fetch failed: " + error.message);
    }
  });

  it("should update the user information", async () => {
    try {
      expect(token).not.toBeNull();

      const updatePayload = {
        email: "jasmine_updated@test.com",
        username: "jasmine_updated",
        password: "jasmine"
      };

      const response = await axios.put(`${path}api/users/${id}`, updatePayload, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      expect(response.status).toBe(200);
      expect(response.data).toBe("Utilisateur mis à jour avec succès.");

      console.log("Update response:", response.data);
    } catch (error) {
      fail("Update failed: " + (error.response?.data?.message || error.message));
    }
  });

  it("should NOT allow user to update another user's information", async () => {
    try {
      expect(token).not.toBeNull();

      const updatePayload = {
        email: "unauthorized_update@test.com",
        username: "unauthorized_update",
        password: "jasmine"
      };

      const response = await axios.put(`${path}api/users/1`, updatePayload, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      fail("Expected 403 Forbidden but request succeeded with status " + response.status);
    } catch (error) {

      expect(error.response.status).toBe(403);
      console.log("Correctly blocked update on another user's account:", error.response.data);
    }
  });

  it("should update the user information", async () => {
    try {
      expect(token).not.toBeNull();

      const updatePayload = {
        email: "jasmine@test.com",
        username: "jasmine",
        password: "jasmine"
      };

      const response = await axios.put(`${path}api/users/${id}`, updatePayload, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      expect(response.status).toBe(200);
      expect(response.data).toBe("Utilisateur mis à jour avec succès.");

      console.log("Update response:", response.data);
    } catch (error) {
      fail("Update failed: " + (error.response?.data?.message || error.message));
    }
  });

    it("should fail to update the user with an email already used by another user", async () => {
    try {
      expect(token).not.toBeNull();

      const updatePayload = {
        email: "angel@test.com",
        username: "jasmine_conflict",
        password: "jasmine"
      };

      await axios.put(`${path}api/users/${id}`, updatePayload, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      fail("Request should have failed due to email conflict, but it succeeded.");
    } catch (error) {
      const status = error.response?.status;
      const message = error.response?.data?.message;

      expect(status).toBe(403);
      expect(message).toBe(undefined);
      console.log("Email conflict correctly caught:", message);
    }
  });

  it("should fail to update the user with an username already used by another user", async () => {
    try {
      expect(token).not.toBeNull();

      const updatePayload = {
        email: "jasmine_conflict@test.com",
        username: "angel",
        password: "jasmine"
      };

      await axios.put(`${path}api/users/${id}`, updatePayload, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      fail("Request should have failed due to username conflict, but it succeeded.");
    } catch (error) {
      const status = error.response?.status;
      const message = error.response?.data?.message;

      expect(status).toBe(403);
      expect(message).toBe(undefined);
      console.log("Email conflict correctly caught:", message);
    }
  });

  it("should return the user's trajets if they exist", async () => {
    try {
      expect(token).not.toBeNull();

      const response = await axios.get(`${path}api/users/trajets`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      expect(response.status).toBe(200);
      expect(response.data).toBeDefined();
      expect(Array.isArray(response.data.trajets)).toBeTrue();

      console.log("User's trajets:", response.data.trajets);
    } catch (error) {
      fail("Fetching trajets failed: " + (error.response?.data?.message || error.message));
    }
  });


});
