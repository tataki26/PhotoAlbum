import { apiClient } from "./ApiClient";

export const addPhotoApi
    = (id, formData) => apiClient.post(`/albums/${id}/photos/v2`, formData);

export const movePhotoApi
    = (id, photo) => apiClient.put(`/albums/${id}/photos/move/v2`, photo);

export const deletePhotoApi
    = (id, photoIds) => apiClient.delete(`/albums/${id}/photos/v2`, {
        data: {photoIds}
    });

export const downloadPhotoApi
    = async (id, queryParams) => {
        const response = await apiClient.get(`/albums/${id}/photos/download/v2`, {
            params: queryParams,
            responseType: 'blob',
        });
        return response;
    };
    