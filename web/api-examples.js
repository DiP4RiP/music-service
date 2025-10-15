// Примеры использования API для Music Service
// Этот файл содержит примеры кода для работы с API

// Пример 1: Регистрация нового пользователя
async function registerUser() {
    const userData = {
        fullName: "Иван Иванов",
        address: "Москва, ул. Примерная, д. 1",
        phone: "+7 (999) 123-45-67",
        login: "ivan.ivanov",
        password: "password123"
    };
    
    try {
        const response = await fetch('http://localhost:8080/api/user', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });
        
        if (response.ok) {
            const user = await response.json();
            console.log('Пользователь зарегистрирован:', user);
        } else {
            console.error('Ошибка регистрации:', response.statusText);
        }
    } catch (error) {
        console.error('Ошибка сети:', error);
    }
}

// Пример 2: Аутентификация пользователя
async function authenticateUser(username, password) {
    const token = btoa(`${username}:${password}`);
    
    try {
        const response = await fetch('http://localhost:8080/api/user', {
            method: 'GET',
            headers: {
                'Authorization': `Basic ${token}`
            }
        });
        
        if (response.ok) {
            const users = await response.json();
            const user = users.find(u => u.login === username);
            console.log('Пользователь найден:', user);
            return { user, token };
        } else {
            console.error('Ошибка аутентификации:', response.statusText);
        }
    } catch (error) {
        console.error('Ошибка сети:', error);
    }
}

// Пример 3: Получение списка музыки
async function getMusicList() {
    try {
        const response = await fetch('http://localhost:8080/api/music', {
            headers: {
                'Authorization': `Basic ${authToken}`
            }
        });
        
        if (response.ok) {
            const music = await response.json();
            console.log('Список музыки:', music);
            return music;
        } else {
            console.error('Ошибка загрузки музыки:', response.statusText);
        }
    } catch (error) {
        console.error('Ошибка сети:', error);
    }
}

// Пример 4: Получение информации о конкретной песне
async function getMusicDetails(inventoryNumber) {
    try {
        const response = await fetch(`http://localhost:8080/api/music/${inventoryNumber}`, {
            headers: {
                'Authorization': `Basic ${authToken}`
            }
        });
        
        if (response.ok) {
            const music = await response.json();
            console.log('Детали музыки:', music);
            return music;
        } else {
            console.error('Ошибка загрузки деталей:', response.statusText);
        }
    } catch (error) {
        console.error('Ошибка сети:', error);
    }
}

// Пример 5: Создание плейлиста
async function createPlaylist(name, userId) {
    const playlistData = {
        name: name,
        userId: userId
    };
    
    try {
        const response = await fetch('http://localhost:8080/api/playlist', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Basic ${authToken}`
            },
            body: JSON.stringify(playlistData)
        });
        
        if (response.ok) {
            const playlist = await response.json();
            console.log('Плейлист создан:', playlist);
            return playlist;
        } else {
            console.error('Ошибка создания плейлиста:', response.statusText);
        }
    } catch (error) {
        console.error('Ошибка сети:', error);
    }
}

// Пример 6: Добавление музыки в плейлист
async function addMusicToPlaylist(playlistId, inventoryNumbers) {
    const musicData = {
        playlistId: playlistId,
        inventoryNumbers: inventoryNumbers
    };
    
    try {
        const response = await fetch('http://localhost:8080/api/playlist/music', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Basic ${authToken}`
            },
            body: JSON.stringify(musicData)
        });
        
        if (response.ok) {
            const playlist = await response.json();
            console.log('Музыка добавлена в плейлист:', playlist);
            return playlist;
        } else {
            console.error('Ошибка добавления музыки:', response.statusText);
        }
    } catch (error) {
        console.error('Ошибка сети:', error);
    }
}

// Пример 7: Получение списка плейлистов
async function getPlaylists() {
    try {
        const response = await fetch('http://localhost:8080/api/playlist', {
            headers: {
                'Authorization': `Basic ${authToken}`
            }
        });
        
        if (response.ok) {
            const playlists = await response.json();
            console.log('Список плейлистов:', playlists);
            return playlists;
        } else {
            console.error('Ошибка загрузки плейлистов:', response.statusText);
        }
    } catch (error) {
        console.error('Ошибка сети:', error);
    }
}

// Пример 8: Получение информации о плейлисте
async function getPlaylistDetails(playlistId) {
    try {
        const response = await fetch(`http://localhost:8080/api/playlist/${playlistId}`, {
            headers: {
                'Authorization': `Basic ${authToken}`
            }
        });
        
        if (response.ok) {
            const playlist = await response.json();
            console.log('Детали плейлиста:', playlist);
            return playlist;
        } else {
            console.error('Ошибка загрузки деталей плейлиста:', response.statusText);
        }
    } catch (error) {
        console.error('Ошибка сети:', error);
    }
}

// Пример 9: Админ - добавление новой музыки
async function addMusic(musicData) {
    try {
        const response = await fetch('http://localhost:8080/api/admin/music', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Basic ${authToken}`
            },
            body: JSON.stringify(musicData)
        });
        
        if (response.ok) {
            const music = await response.json();
            console.log('Музыка добавлена:', music);
            return music;
        } else {
            console.error('Ошибка добавления музыки:', response.statusText);
        }
    } catch (error) {
        console.error('Ошибка сети:', error);
    }
}

// Пример 10: Админ - удаление музыки
async function deleteMusic(inventoryNumber) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/music/${inventoryNumber}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Basic ${authToken}`
            }
        });
        
        if (response.ok) {
            console.log('Музыка удалена');
            return true;
        } else {
            console.error('Ошибка удаления музыки:', response.statusText);
        }
    } catch (error) {
        console.error('Ошибка сети:', error);
    }
}

// Пример 11: Админ - получение всех пользователей
async function getAllUsers() {
    try {
        const response = await fetch('http://localhost:8080/api/admin/users', {
            headers: {
                'Authorization': `Basic ${authToken}`
            }
        });
        
        if (response.ok) {
            const users = await response.json();
            console.log('Список пользователей:', users);
            return users;
        } else {
            console.error('Ошибка загрузки пользователей:', response.statusText);
        }
    } catch (error) {
        console.error('Ошибка сети:', error);
    }
}

// Пример 12: Админ - получение информации о пользователе
async function getUserDetails(userId) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/users/${userId}`, {
            headers: {
                'Authorization': `Basic ${authToken}`
            }
        });
        
        if (response.ok) {
            const user = await response.json();
            console.log('Детали пользователя:', user);
            return user;
        } else {
            console.error('Ошибка загрузки деталей пользователя:', response.statusText);
        }
    } catch (error) {
        console.error('Ошибка сети:', error);
    }
}

// Пример 13: Админ - удаление пользователя
async function deleteUser(userId) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/users/${userId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Basic ${authToken}`
            }
        });
        
        if (response.ok) {
            console.log('Пользователь удален');
            return true;
        } else {
            console.error('Ошибка удаления пользователя:', response.statusText);
        }
    } catch (error) {
        console.error('Ошибка сети:', error);
    }
}

// Пример использования всех функций
async function exampleUsage() {
    // 1. Регистрация пользователя
    await registerUser();
    
    // 2. Аутентификация
    const { user, token } = await authenticateUser('ivan.ivanov', 'password123');
    authToken = token;
    
    // 3. Получение списка музыки
    const music = await getMusicList();
    
    // 4. Создание плейлиста
    const playlist = await createPlaylist('Мой плейлист', user.id);
    
    // 5. Добавление музыки в плейлист
    if (music && music.length > 0) {
        await addMusicToPlaylist(playlist.id, [music[0].inventoryNumber]);
    }
    
    // 6. Получение плейлистов
    const playlists = await getPlaylists();
    
    // Если пользователь админ
    if (user.role === 'ADMIN') {
        // 7. Добавление новой музыки
        const newMusic = {
            title: 'Новая песня',
            genreId: 1,
            performerId: 1,
            composerId: 1,
            mediaTypeId: 1,
            recordLabelId: 1,
            recordingDate: '2024-01-01'
        };
        await addMusic(newMusic);
        
        // 8. Получение всех пользователей
        const users = await getAllUsers();
        
        // 9. Удаление пользователя (если нужно)
        // await deleteUser(users[0].id);
    }
}

// Экспорт функций для использования в других файлах
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        registerUser,
        authenticateUser,
        getMusicList,
        getMusicDetails,
        createPlaylist,
        addMusicToPlaylist,
        getPlaylists,
        getPlaylistDetails,
        addMusic,
        deleteMusic,
        getAllUsers,
        getUserDetails,
        deleteUser
    };
}
