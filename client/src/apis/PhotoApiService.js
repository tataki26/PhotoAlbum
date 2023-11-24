import { apiClient } from "./ApiClient";

export const movePhotoApi
    = (id, photo) => apiClient.put(`/albums/${id}/photos/move`, photo)