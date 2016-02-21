angular.module('app').service('User', userService);

function userService($window, $location, $http, EventHandler) {
	const ROL_ADMIN = 'Administrador';
	const ROL_TUTOR = 'Tutor';
	const ROL_ESTUDIANTE = 'Estudiante';
	var user = null;
	var self = this;

	self.atributos = [{
		displayHeader: 'Nombres',
		columnAtrubute: 'Nombres',
		show: true,
		editable: true,
		editor: 'text'
	}, {
		displayHeader: 'Correo',
		columnAtrubute: 'Correo',
		show: true,
		editable: true,
		editor: 'text'
	}, {
		displayHeader: 'Usuario',
		columnAtrubute: 'Usuario',
		show: true,
		editable: false,
		editor: 'text'
	}, {
		displayHeader: 'Sección',
		columnAtrubute: 'Seccion',
		show: true,
		editable: true,
		editor: 'text'
	}, {
		displayHeader: 'Codigo',
		columnAtrubute: 'Codigo',
		show: true,
		editable: false,
	}];
	self.atributosMinimos = [{
		displayHeader: 'Nombres',
		columnAtrubute: 'Nombres',
		show: true,
		editable: true,
		editor: 'text'
	}, {
		displayHeader: 'Usuario',
		columnAtrubute: 'Usuario',
		show: true,
		editable: false,
		editor: 'text'
	}, {
		displayHeader: 'Codigo',
		columnAtrubute: 'Codigo',
		show: true,
		editable: false,
	}];
	self.redirectInic = function() {
		console.log("redirectInic", user)
		if (!user) {
			console.log("No user")
			//$location.url('/');
			$window.location.reload();
		} else if (self.hasRol(ROL_ADMIN))
		$location.url('/admin')
		else if (self.hasRol(ROL_TUTOR))
			$location.url('/tutor')
		else {
			$location.url('/reservas')
			console.log("User loggedin as student")
		}
	}
	self.allow = {
		EST: true,
		TUT: true,
		ADM: true
	}
	self.hasRol = function(rol) {
		if (!user)
			return false;

		var roles = user.roles;
		console.log(roles)
		for (var i = 0; i < roles.length; i++) {
			if (roles[i].Nombre === rol) {
				return true;
			}
		};
		return false;
	}
	self.getUserName = function(){
		if(user)
			return user.Nombres;
		else
			return null;
	}
	self.getUser = function() {
		return user;
	}
	self.setUser = function(newUser) {
		console.log("Nuevo Usuario: ", newUser);
		user = newUser;
		self.updatePermits();
		self.redirectInic();
	}
	self.login = function(userLogin){
		$http.post('/login', userLogin)
		.success(function(data, status, headers, config) {
			self.setUser(data);
		})
		.error(EventHandler.error);
	};
	self.checkLoggedin = function(userLogin){
		$http.get('/loggedin').success(function(data, status, headers, config){ 
			if (data !== '0') {
				self.setUser(data);
			}
		});
	};
	self.logout = function(){
		$http.post('/logout', {})
		.success(function(data, status, headers, config) {
			self.setUser(null);
		})
		.error(function(data, status, headers, config) {
			console.error(data);
			EventHandler.error(data, status, headers, config)
		});
	}
	self.updatePermits = function(){
		self.allow.EST = self.hasRol(ROL_ESTUDIANTE);
		self.allow.TUT = self.hasRol(ROL_TUTOR);
		self.allow.ADM = self.hasRol(ROL_ADMIN);
	}

	self.getAsistentes = function(callback) {
		$http.get('/usuarios/asistentes')
		.success(function(data, status, headers, config) {
			callback(data);
		}).error(EventHandler.error);
	}
	self.getTutores = function(callback) {
		$http.get('/usuarios/tutores')
		.success(function(data, status, headers, config) {
			callback(data);
		}).error(EventHandler.error);
	}
	
}