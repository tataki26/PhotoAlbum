import { apiClient } from "./ApiClient";

export const createMemberApi
    = (member) => apiClient.post('/users', member);