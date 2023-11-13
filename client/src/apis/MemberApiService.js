import { apiClient } from "./ApiClient";

export const createMemberApi
    = (member) => apiClient.post('/users', member);

export const loginApi
    = (member) => apiClient.post('/sign-in', member);