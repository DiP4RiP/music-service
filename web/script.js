// Глобальные переменные
let currentUser = null;
let authToken = null;
const API_BASE_URL = 'http://localhost:8080/api';

// Утилиты для работы с API
class ApiClient {
    static async request(url, options = {}) {
        const config = {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        };

        if (authToken) {
            config.headers['Authorization'] = `Basic ${authToken}`;
        }

        try {
            const response = await fetch(`${API_BASE_URL}${url}`, config);
            
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`HTTP ${response.status}: ${errorText}`);
            }

            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            }
            return null;
        } catch (error) {
            console.error('API Error:', error);
            showNotification('Ошибка при обращении к серверу: ' + error.message, 'error');
            throw error;
        }
    }

    static async get(url) {
        return this.request(url, { method: 'GET' });
    }

    static async post(url, data) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    static async delete(url) {
        return this.request(url, { method: 'DELETE' });
    }
}

// Утилиты для уведомлений
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    document.body.appendChild(notification);

    setTimeout(() => {
        notification.remove();
    }, 5000);
}

// Утилиты для работы с localStorage
function saveAuthData(user, token) {
    localStorage.setItem('currentUser', JSON.stringify(user));
    localStorage.setItem('authToken', token);
}

function loadAuthData() {
    const user = localStorage.getItem('currentUser');
    const token = localStorage.getItem('authToken');
    
    if (user && token) {
        currentUser = JSON.parse(user);
        authToken = token;
        return true;
    }
    return false;
}

function clearAuthData() {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('authToken');
    currentUser = null;
    authToken = null;
}

// Проверка авторизации
function checkAuth() {
    if (!currentUser) {
        if (loadAuthData()) {
            updateUI();
            return true;
        }
        return false;
    }
    return true;
}

// Обновление UI в зависимости от состояния авторизации
function updateUI() {
    const authSection = document.getElementById('authSection');
    const mainContent = document.getElementById('mainContent');
    const authCheck = document.getElementById('authCheck');
    const accessDenied = document.getElementById('accessDenied');
    const adminContent = document.getElementById('adminContent');
    const playlistsContent = document.getElementById('playlistsContent');

    if (currentUser) {
        if (authSection) authSection.style.display = 'none';
        if (mainContent) mainContent.style.display = 'block';
        if (authCheck) authCheck.style.display = 'none';
        if (accessDenied) accessDenied.style.display = 'none';
        if (adminContent) adminContent.style.display = 'block';
        if (playlistsContent) playlistsContent.style.display = 'block';

        // Обновляем информацию о пользователе
        const userInfo = document.getElementById('userInfo');
        if (userInfo) {
            userInfo.textContent = `Добро пожаловать, ${currentUser.fullName}!`;
        }
        

    // Проверяем права администратора
    if (window.location.pathname.includes('admin.html')) {
      console.log('Проверка прав администратора. Роль пользователя:', currentUser.role);
      if (currentUser.role !== 'ADMIN') {
        console.log('Доступ запрещен: пользователь не является администратором');
        if (accessDenied) accessDenied.style.display = 'block';
        if (adminContent) adminContent.style.display = 'none';
      } else {
        console.log('Доступ разрешен: пользователь является администратором');
      }
    }
    
    } else {
        if (authSection) authSection.style.display = 'block';
        if (mainContent) mainContent.style.display = 'none';
        if (authCheck) authCheck.style.display = 'block';
        if (accessDenied) accessDenied.style.display = 'none';
        if (adminContent) adminContent.style.display = 'none';
        if (playlistsContent) playlistsContent.style.display = 'none';
    }
}

// Аутентификация
async function login(username, password) {
    try {
        const token = btoa(`${username}:${password}`);
        authToken = token;
        
        // Получаем информацию о пользователе
        const users = await ApiClient.get('/user');
        const user = users.find(u => u.login === username);
        
        if (user) {
            currentUser = user;
            console.log('Пользователь авторизован:', user);
            console.log('Роль пользователя:', user.role);
            saveAuthData(user, token);
            updateUI();
            showNotification('Успешный вход в систему!', 'success');
            
            // Загружаем данные в зависимости от страницы
            if (window.location.pathname.includes('index.html')) {
                loadMusic();
            } else if (window.location.pathname.includes('playlists.html')) {
                loadPlaylists();
            } else if (window.location.pathname.includes('admin.html')) {
                loadAdminData();
            }
            
            return true;
        } else {
            throw new Error('Пользователь не найден');
        }
    } catch (error) {
        showNotification('Ошибка входа: ' + error.message, 'error');
        return false;
    }
}

async function register(userData) {
    try {
        const newUser = await ApiClient.post('/user', userData);
        showNotification('Регистрация успешна! Теперь вы можете войти в систему.', 'success');
        
        // Переключаемся на вкладку входа
        const loginTab = document.querySelector('[data-tab="login"]');
        if (loginTab) loginTab.click();
        
        return true;
    } catch (error) {
        showNotification('Ошибка регистрации: ' + error.message, 'error');
        return false;
    }
}

function logout() {
    clearAuthData();
    updateUI();
    showNotification('Вы вышли из системы', 'info');
    
    // Перенаправляем на главную страницу
    if (!window.location.pathname.includes('index.html')) {
        window.location.href = 'index.html';
    }
}

// Загрузка музыки
async function loadMusic() {
    const musicList = document.getElementById('musicList');
    if (!musicList) return;

    try {
        musicList.innerHTML = '<div class="loading"><div class="spinner"></div></div>';
        
        const music = await ApiClient.get('/music');
        
        if (music && music.length > 0) {
            musicList.innerHTML = music.map(song => `
                <div class="music-card" onclick="showMusicDetails(${song.inventoryNumber})">
                    <h3>${song.title}</h3>
                    <p><strong>Исполнитель:</strong> ${song.performer}</p>
                    <p><strong>Композитор:</strong> ${song.composerName}</p>
                    <p><strong>Жанр:</strong> ${song.genre}</p>
                    <p><strong>Дата записи:</strong> ${song.recordingDate}</p>
                </div>
            `).join('');
        } else {
            musicList.innerHTML = '<p>Музыка не найдена</p>';
        }
    } catch (error) {
        musicList.innerHTML = '<p>Ошибка загрузки музыки</p>';
    }
}

// Поиск музыки
async function searchMusic(query) {
    const musicList = document.getElementById('musicList');
    if (!musicList) return;

    try {
        musicList.innerHTML = '<div class="loading"><div class="spinner"></div></div>';
        
        const music = await ApiClient.get(`/music/search?query=${encodeURIComponent(query)}`);
        
        if (music && music.length > 0) {
            musicList.innerHTML = music.map(song => `
                <div class="music-card" onclick="showMusicDetails(${song.inventoryNumber})">
                    <h3>${song.title}</h3>
                    <p><strong>Исполнитель:</strong> ${song.performer}</p>
                    <p><strong>Композитор:</strong> ${song.composerName}</p>
                    <p><strong>Жанр:</strong> ${song.genre}</p>
                    <p><strong>Дата записи:</strong> ${song.recordingDate}</p>
                </div>
            `).join('');
        } else {
            musicList.innerHTML = '<p>По вашему запросу ничего не найдено</p>';
        }
    } catch (error) {
        musicList.innerHTML = '<p>Ошибка поиска музыки</p>';
    }
}

// Показать детали музыки
async function showMusicDetails(inventoryNumber) {
    try {
        const music = await ApiClient.get(`/music/${inventoryNumber}`);
        const modal = document.getElementById('musicModal');
        const details = document.getElementById('musicDetails');
        
        if (music) {
            details.innerHTML = `
                <h3>${music.title}</h3>
                <div class="music-details">
                    <p><strong>Исполнитель:</strong> ${music.performer}</p>
                    <p><strong>Композитор:</strong> ${music.composerName}</p>
                    <p><strong>Жанр:</strong> ${music.genre}</p>
                    <p><strong>Тип носителя:</strong> ${music.mediaTypeName}</p>
                    <p><strong>Студия записи:</strong> ${music.recordLabelName}</p>
                    <p><strong>Дата записи:</strong> ${music.recordingDate}</p>
                    <p><strong>Инвентарный номер:</strong> ${music.inventoryNumber}</p>
                </div>
            `;
        }
        
        modal.style.display = 'block';
    } catch (error) {
        showNotification('Ошибка загрузки деталей музыки: ' + error.message, 'error');
    }
}

// Загрузка плейлистов
async function loadPlaylists() {
    const playlistsList = document.getElementById('playlistsList');
    if (!playlistsList) return;

    try {
        playlistsList.innerHTML = '<div class="loading"><div class="spinner"></div></div>';
        
        const playlists = await ApiClient.get('/playlist');
        
        if (playlists && playlists.length > 0) {
            playlistsList.innerHTML = playlists.map(playlist => {
                const isOwner = playlist.user.id === currentUser.id;
                return `
                    <div class="playlist-card" onclick="showPlaylistDetails(${playlist.id})">
                        <h3>${playlist.name}</h3>
                        <p><strong>Владелец:</strong> ${playlist.user.fullName}</p>
                        <p><strong>Количество песен:</strong> ${playlist.musicList ? playlist.musicList.length : 0}</p>
                        ${isOwner ? `
                            <div class="playlist-actions">
                                <button class="btn btn-primary" onclick="event.stopPropagation(); addMusicToPlaylist(${playlist.id})">
                                    Добавить музыку
                                </button>
                            </div>
                        ` : `
                            <div class="playlist-info">
                                <span class="read-only">Только для просмотра</span>
                            </div>
                        `}
                    </div>
                `;
            }).join('');
        } else {
            playlistsList.innerHTML = '<p>Плейлисты не найдены</p>';
        }
    } catch (error) {
        playlistsList.innerHTML = '<p>Ошибка загрузки плейлистов</p>';
    }
}

// Показать детали плейлиста
async function showPlaylistDetails(playlistId) {
    try {
        const playlist = await ApiClient.get(`/playlist/${playlistId}`);
        const modal = document.getElementById('playlistModal');
        const details = document.getElementById('playlistDetails');
        
        if (playlist) {
            const musicList = playlist.musicList ? playlist.musicList.map(music => `
                <div class="music-item">
                    <div class="music-item-info">
                        <h4>${music.title}</h4>
                        <p>${music.performer} - ${music.composerName}</p>
                    </div>
                </div>
            `).join('') : '<p>В плейлисте нет музыки</p>';
            
            const isOwner = playlist.user.id === currentUser.id;
            details.innerHTML = `
                <h3>${playlist.name}</h3>
                <div class="playlist-details">
                    <p><strong>Владелец:</strong> ${playlist.user.fullName}</p>
                    <p><strong>Количество песен:</strong> ${playlist.musicList ? playlist.musicList.length : 0}</p>
                </div>
                <div class="playlist-music">
                    <h4>Музыка в плейлисте:</h4>
                    <div class="music-selection">
                        ${musicList}
                    </div>
                </div>
                ${isOwner ? `
                    <div class="playlist-actions">
                        <button class="btn btn-primary" onclick="addMusicToPlaylist(${playlist.id})">
                            Добавить музыку
                        </button>
                    </div>
                ` : `
                    <div class="playlist-info">
                        <span class="read-only">Только для просмотра</span>
                    </div>
                `}
            `;
        }
        
        modal.style.display = 'block';
    } catch (error) {
        showNotification('Ошибка загрузки деталей плейлиста: ' + error.message, 'error');
    }
}

// Добавить в плейлист
function addToPlaylist(inventoryNumber) {
    const modal = document.getElementById('playlistModal');
    const selection = document.getElementById('playlistSelection');
    
    // Загружаем плейлисты для выбора
    loadPlaylistsForSelection(inventoryNumber);
    
    modal.style.display = 'block';
}

// Загрузить плейлисты для выбора
async function loadPlaylistsForSelection(inventoryNumber) {
    const selection = document.getElementById('playlistSelection');
    if (!selection) return;

    try {
        selection.innerHTML = '<div class="loading"><div class="spinner"></div></div>';
        
        const playlists = await ApiClient.get('/playlist');
        
        if (playlists && playlists.length > 0) {
            selection.innerHTML = playlists.map(playlist => `
                <div class="playlist-item" onclick="addMusicToPlaylist(${playlist.id}, ${inventoryNumber})">
                    <h4>${playlist.name}</h4>
                    <p>Владелец: ${playlist.user.fullName}</p>
                </div>
            `).join('');
        } else {
            selection.innerHTML = '<p>Нет доступных плейлистов</p>';
        }
    } catch (error) {
        selection.innerHTML = '<p>Ошибка загрузки плейлистов</p>';
    }
}

// Добавить музыку в плейлист
async function addMusicToPlaylist(playlistId, inventoryNumber = null) {
    if (!inventoryNumber) {
        // Показать модальное окно для выбора музыки
        const modal = document.getElementById('addMusicModal');
        modal.style.display = 'block';
        loadAvailableMusic(playlistId);
        return;
    }

    try {
        await ApiClient.post('/playlist/music', {
            playlistId: playlistId,
            inventoryNumbers: [inventoryNumber]
        });
        
        showNotification('Музыка успешно добавлена в плейлист!', 'success');
        
        // Закрываем модальные окна
        const playlistModal = document.getElementById('playlistModal');
        const addMusicModal = document.getElementById('addMusicModal');
        if (playlistModal) playlistModal.style.display = 'none';
        if (addMusicModal) addMusicModal.style.display = 'none';
        
        // Обновляем список плейлистов
        if (window.location.pathname.includes('playlists.html')) {
            loadPlaylists();
        }
    } catch (error) {
        if (error.message.includes('Доступ запрещен')) {
            showNotification('Вы можете добавлять музыку только в свои плейлисты!', 'error');
        } else {
            showNotification('Ошибка добавления музыки в плейлист: ' + error.message, 'error');
        }
    }
}

// Загрузить доступную музыку
async function loadAvailableMusic(playlistId) {
    const musicContainer = document.getElementById('availableMusic');
    if (!musicContainer) return;

    try {
        musicContainer.innerHTML = '<div class="loading"><div class="spinner"></div></div>';
        
        const music = await ApiClient.get('/music');
        
        if (music && music.length > 0) {
            musicContainer.innerHTML = music.map(song => `
                <div class="music-item">
                    <div class="music-item-info">
                        <h4>${song.title}</h4>
                        <p>${song.performer} - ${song.composerName}</p>
                    </div>
                    <button class="btn btn-primary" onclick="addMusicToPlaylist(${playlistId}, ${song.inventoryNumber})">
                        Добавить
                    </button>
                </div>
            `).join('');
        } else {
            musicContainer.innerHTML = '<p>Нет доступной музыки</p>';
        }
    } catch (error) {
        musicContainer.innerHTML = '<p>Ошибка загрузки музыки</p>';
    }
}

// Создать плейлист
async function createPlaylist(name) {
    try {
        const playlistData = {
            name: name,
            userId: currentUser.id
        };
        
        await ApiClient.post('/playlist', playlistData);
        showNotification('Плейлист успешно создан!', 'success');
        
        // Закрываем модальное окно
        document.getElementById('createPlaylistModal').style.display = 'none';
        
        // Очищаем форму
        document.getElementById('createPlaylistForm').reset();
        
        // Обновляем список плейлистов
        loadPlaylists();
    } catch (error) {
        showNotification('Ошибка создания плейлиста: ' + error.message, 'error');
    }
}

// Админ функции
async function loadAdminData() {
    if (currentUser.role !== 'ADMIN') return;
    
    // Загружаем музыку для админ панели
    await loadAdminMusic();
    
    // Загружаем пользователей для админ панели
    await loadAdminUsers();
}

async function loadAdminMusic() {
    const musicList = document.getElementById('adminMusicList');
    if (!musicList) return;

    try {
        musicList.innerHTML = '<div class="loading"><div class="spinner"></div></div>';
        
        const music = await ApiClient.get('/music');
        
        if (music && music.length > 0) {
            musicList.innerHTML = music.map(song => `
                <div class="admin-card">
                    <h3>${song.title}</h3>
                    <p><strong>Исполнитель:</strong> ${song.performer}</p>
                    <p><strong>Композитор:</strong> ${song.composerName}</p>
                    <p><strong>Жанр:</strong> ${song.genre}</p>
                    <p><strong>Дата записи:</strong> ${song.recordingDate}</p>
                    <div class="admin-actions">
                        <button class="btn btn-danger" onclick="deleteMusic(${song.inventoryNumber})">
                            Удалить
                        </button>
                    </div>
                </div>
            `).join('');
        } else {
            musicList.innerHTML = '<p>Музыка не найдена</p>';
        }
    } catch (error) {
        musicList.innerHTML = '<p>Ошибка загрузки музыки</p>';
    }
}

// Поиск музыки в админ панели
async function searchAdminMusic(query) {
    const musicList = document.getElementById('adminMusicList');
    if (!musicList) return;

    try {
        musicList.innerHTML = '<div class="loading"><div class="spinner"></div></div>';
        
        const music = await ApiClient.get(`/music/search?query=${encodeURIComponent(query)}`);
        
        if (music && music.length > 0) {
            musicList.innerHTML = music.map(song => `
                <div class="admin-card">
                    <h3>${song.title}</h3>
                    <p><strong>Исполнитель:</strong> ${song.performer}</p>
                    <p><strong>Композитор:</strong> ${song.composerName}</p>
                    <p><strong>Жанр:</strong> ${song.genre}</p>
                    <p><strong>Дата записи:</strong> ${song.recordingDate}</p>
                    <div class="admin-actions">
                        <button class="btn btn-danger" onclick="deleteMusic(${song.inventoryNumber})">
                            Удалить
                        </button>
                    </div>
                </div>
            `).join('');
        } else {
            musicList.innerHTML = '<p>По вашему запросу ничего не найдено</p>';
        }
    } catch (error) {
        musicList.innerHTML = '<p>Ошибка поиска музыки</p>';
    }
}

async function loadAdminUsers() {
    const usersList = document.getElementById('adminUsersList');
    if (!usersList) return;

    try {
        usersList.innerHTML = '<div class="loading"><div class="spinner"></div></div>';
        
        const users = await ApiClient.get('/admin/users');
        
        if (users && users.length > 0) {
            usersList.innerHTML = users.map(user => `
                <div class="admin-card">
                    <h3>${user.fullName}</h3>
                    <p><strong>Логин:</strong> ${user.login}</p>
                    <p><strong>Адрес:</strong> ${user.address}</p>
                    <p><strong>Телефон:</strong> ${user.phone}</p>
                    <div class="admin-actions">
                        <button class="btn btn-primary" onclick="showUserDetails(${user.id})">
                            Подробнее
                        </button>
                        <button class="btn btn-danger" onclick="deleteUser(${user.id})">
                            Удалить
                        </button>
                    </div>
                </div>
            `).join('');
        } else {
            usersList.innerHTML = '<p>Пользователи не найдены</p>';
        }
    } catch (error) {
        usersList.innerHTML = '<p>Ошибка загрузки пользователей</p>';
    }
}

// Добавить музыку (админ)
async function addMusic(musicData) {
    try {
        await ApiClient.post('/admin/music', musicData);
        showNotification('Музыка успешно добавлена!', 'success');
        
        // Закрываем модальное окно
        document.getElementById('addMusicModal').style.display = 'none';
        
        // Очищаем форму
        document.getElementById('addMusicForm').reset();
        
        // Обновляем список музыки
        loadAdminMusic();
    } catch (error) {
        showNotification('Ошибка добавления музыки: ' + error.message, 'error');
    }
}

// Удалить музыку (админ)
async function deleteMusic(inventoryNumber) {
    if (!confirm('Вы уверены, что хотите удалить эту музыку?')) return;
    
    try {
        await ApiClient.delete(`/admin/music/${inventoryNumber}`);
        showNotification('Музыка успешно удалена!', 'success');
        loadAdminMusic();
    } catch (error) {
        showNotification('Ошибка удаления музыки: ' + error.message, 'error');
    }
}

// Показать детали пользователя (админ)
async function showUserDetails(userId) {
    try {
        const user = await ApiClient.get(`/admin/users/${userId}`);
        const modal = document.getElementById('userModal');
        const details = document.getElementById('userDetails');
        
        if (user) {
            details.innerHTML = `
                <h3>${user.fullName}</h3>
                <div class="user-details">
                    <p><strong>ID:</strong> ${user.id}</p>
                    <p><strong>Логин:</strong> ${user.login}</p>
                    <p><strong>Адрес:</strong> ${user.address}</p>
                    <p><strong>Телефон:</strong> ${user.phone}</p>
                </div>
                <div class="user-actions">
                    <button class="btn btn-danger" onclick="deleteUser(${user.id})">
                        Удалить пользователя
                    </button>
                </div>
            `;
        }
        
        modal.style.display = 'block';
    } catch (error) {
        showNotification('Ошибка загрузки деталей пользователя: ' + error.message, 'error');
    }
}

// Удалить пользователя (админ)
async function deleteUser(userId) {
    if (!confirm('Вы уверены, что хотите удалить этого пользователя?')) return;
    
    try {
        await ApiClient.delete(`/admin/users/${userId}`);
        showNotification('Пользователь успешно удален!', 'success');
        loadAdminUsers();
        
        // Закрываем модальное окно если открыто
        const modal = document.getElementById('userModal');
        if (modal) modal.style.display = 'none';
    } catch (error) {
        showNotification('Ошибка удаления пользователя: ' + error.message, 'error');
    }
}

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    // Проверяем авторизацию
    checkAuth();
    updateUI();
    
    // Обработчики для форм входа/регистрации
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const formData = new FormData(loginForm);
            const username = formData.get('login');
            const password = formData.get('password');
            await login(username, password);
        });
    }
    
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const formData = new FormData(registerForm);
            const userData = {
                fullName: formData.get('fullName'),
                address: formData.get('address'),
                phone: formData.get('phone'),
                login: formData.get('login'),
                password: formData.get('password')
            };
            await register(userData);
        });
    }
    
    // Обработчики для вкладок аутентификации
    const tabButtons = document.querySelectorAll('.tab-btn');
    tabButtons.forEach(button => {
        button.addEventListener('click', function() {
            const tab = this.dataset.tab;
            
            // Убираем активный класс у всех кнопок и форм
            tabButtons.forEach(btn => btn.classList.remove('active'));
            document.querySelectorAll('.auth-form').forEach(form => form.classList.remove('active'));
            
            // Добавляем активный класс к выбранной кнопке и форме
            this.classList.add('active');
            document.getElementById(tab + 'Form').classList.add('active');
        });
    });
    
    // Обработчик для кнопки выхода
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', logout);
    }
    
    // Обработчики для модальных окон
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        const closeBtn = modal.querySelector('.close');
        if (closeBtn) {
            closeBtn.addEventListener('click', function() {
                modal.style.display = 'none';
            });
        }
        
        // Закрытие по клику вне модального окна
        modal.addEventListener('click', function(e) {
            if (e.target === modal) {
                modal.style.display = 'none';
            }
        });
    });
    
    // Обработчики для админ панели
    const adminTabs = document.querySelectorAll('.admin-tabs .tab-btn');
    adminTabs.forEach(button => {
        button.addEventListener('click', function() {
            const tab = this.dataset.tab;
            
            // Убираем активный класс у всех кнопок и контента
            adminTabs.forEach(btn => btn.classList.remove('active'));
            document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
            
            // Добавляем активный класс к выбранной кнопке и контенту
            this.classList.add('active');
            document.getElementById(tab + 'Tab').classList.add('active');
        });
    });
    
    // Обработчик для создания плейлиста
    const createPlaylistForm = document.getElementById('createPlaylistForm');
    if (createPlaylistForm) {
        createPlaylistForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = new FormData(createPlaylistForm);
            const name = formData.get('name');
            createPlaylist(name);
        });
    }
    
    // Обработчик для добавления музыки (админ)
    const addMusicForm = document.getElementById('addMusicForm');
    if (addMusicForm) {
        addMusicForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = new FormData(addMusicForm);
            const musicData = {
                title: formData.get('title'),
                genreId: parseInt(formData.get('genreId')),
                performerId: parseInt(formData.get('performerId')),
                composerId: parseInt(formData.get('composerId')),
                mediaTypeId: parseInt(formData.get('mediaTypeId')),
                recordLabelId: parseInt(formData.get('recordLabelId')),
                recordingDate: formData.get('recordingDate')
            };
            addMusic(musicData);
        });
    }
    
    // Обработчики для кнопок
    const createPlaylistBtn = document.getElementById('createPlaylistBtn');
    if (createPlaylistBtn) {
        createPlaylistBtn.addEventListener('click', function() {
            document.getElementById('createPlaylistModal').style.display = 'block';
        });
    }
    
    const addMusicBtn = document.getElementById('addMusicBtn');
    if (addMusicBtn) {
        addMusicBtn.addEventListener('click', function() {
            document.getElementById('addMusicModal').style.display = 'block';
        });
    }
    
    // Загружаем данные в зависимости от страницы
    if (window.location.pathname.includes('index.html')) {
        if (currentUser) loadMusic();
    } else if (window.location.pathname.includes('playlists.html')) {
        if (currentUser) loadPlaylists();
    } else if (window.location.pathname.includes('admin.html')) {
        if (currentUser && currentUser.role === 'ADMIN') loadAdminData();
    }
    
    // Обработчики для поиска на главной странице
    const searchBtn = document.getElementById('searchBtn');
    const searchInput = document.getElementById('searchInput');
    if (searchBtn && searchInput) {
        searchBtn.addEventListener('click', function() {
            const query = searchInput.value.trim();
            if (query) {
                searchMusic(query);
            } else {
                loadMusic();
            }
        });
        
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                const query = searchInput.value.trim();
                if (query) {
                    searchMusic(query);
                } else {
                    loadMusic();
                }
            }
        });
    }
    
    // Обработчики для поиска в админ панели
    const adminSearchBtn = document.getElementById('adminSearchBtn');
    const adminMusicSearch = document.getElementById('adminMusicSearch');
    if (adminSearchBtn && adminMusicSearch) {
        adminSearchBtn.addEventListener('click', function() {
            const query = adminMusicSearch.value.trim();
            if (query) {
                searchAdminMusic(query);
            } else {
                loadAdminMusic();
            }
        });
        
        adminMusicSearch.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                const query = adminMusicSearch.value.trim();
                if (query) {
                    searchAdminMusic(query);
                } else {
                    loadAdminMusic();
                }
            }
        });
    }
});
