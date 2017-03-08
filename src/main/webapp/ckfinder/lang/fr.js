/*
 * CKFinder
 * ========
 * http://cksource.com/ckfinder
 * Copyright (C) 2007-2015, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file, and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying, or distributing this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 *
 */

/**
 * @fileOverview Defines the {@link CKFinder.lang} object for the French
 *		language.
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['fr'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, Inaccessible</span>',
		confirmCancel	: 'Certaines options ont \xE9t\xE9 modifi\xE9es. \xCAtes-vous s\xFBr de vouloir fermer cette fen\xEAtre ?',
		ok				: 'OK',
		cancel			: 'Annuler',
		confirmationTitle	: 'Confirmation',
		messageTitle	: 'Information',
		inputTitle		: 'Question',
		undo			: 'Annuler',
		redo			: 'R\xE9tablir',
		skip			: 'Passer',
		skipAll			: 'Passer tout',
		makeDecision	: 'Quelle action choisir ?',
		rememberDecision: 'Se rappeler de la d\xE9cision'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'fr',

	// Date Format
	//		d    : Day
	//		dd   : Day (padding zero)
	//		m    : Month
	//		mm   : Month (padding zero)
	//		yy   : Year (two digits)
	//		yyyy : Year (four digits)
	//		h    : Hour (12 hour clock)
	//		hh   : Hour (12 hour clock, padding zero)
	//		H    : Hour (24 hour clock)
	//		HH   : Hour (24 hour clock, padding zero)
	//		M    : Minute
	//		MM   : Minute (padding zero)
	//		a    : Firt char of AM/PM
	//		aa   : AM/PM
	DateTime : 'dd/mm/yyyy H:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Dossiers',
	FolderLoading	: 'Chargement...',
	FolderNew		: 'Entrez le nouveau nom du dossier: ',
	FolderRename	: 'Entrez le nouveau nom du dossier: ',
	FolderDelete	: '\xCAtes-vous s\xFBr de vouloir effacer le dossier "%1"?',
	FolderRenaming	: ' (Renommage en cours...)',
	FolderDeleting	: ' (Suppression en cours...)',
	DestinationFolder	: 'Dossier de destination',

	// Files
	FileRename		: 'Entrez le nouveau nom du fichier: ',
	FileRenameExt	: '\xCAtes-vous s\xFBr de vouloir changer l\'extension de ce fichier? Le fichier pourrait devenir inutilisable.',
	FileRenaming	: 'Renommage en cours...',
	FileDelete		: '\xCAtes-vous s\xFBr de vouloir effacer le fichier "%1"?',
	FilesDelete	: '\xCAtes-vous s\xFBr de vouloir supprimer %1 fichiers ?',
	FilesLoading	: 'Chargement...',
	FilesEmpty		: 'R\xE9pertoire vide',
	DestinationFile	: 'Fichier de destination',
	SkippedFiles	: 'Liste des fichiers ignor\xE9s : ',

	// Basket
	BasketFolder		: 'Corbeille',
	BasketClear			: 'Vider la corbeille',
	BasketRemove		: 'Retirer de la corbeille',
	BasketOpenFolder	: 'Ouvrir le r\xE9pertiore parent',
	BasketTruncateConfirm : '\xCAtes-vous s\xFBr de vouloir supprimer tous les fichiers de la corbeille ?',
	BasketRemoveConfirm	: '\xCAtes-vous s\xFBr de vouloir supprimer le fichier "%1" de la corbeille ?',
	BasketRemoveConfirmMultiple	: '\xCAtes-vous s\xFBr de vouloir supprimer %1 fichiers de la corbeille ?',
	BasketEmpty			: 'Aucun fichier dans la corbeille, d\xE9posez en queques uns.',
	BasketCopyFilesHere	: 'Copier des fichiers depuis la corbeille',
	BasketMoveFilesHere	: 'D\xE9placer des fichiers depuis la corbeille',

	// Global messages
	OperationCompletedSuccess	: 'Operation termin\xE9e avec succ\xE8s.',
	OperationCompletedErrors		: 'Operation termin\xE9e avec des erreurs.',
	FileError				: '%s: %e',

	// Move and Copy files
	MovedFilesNumber		: 'Nombre de fichiers d\xE9plac\xE9s : %s.',
	CopiedFilesNumber	: 'Nombre de fichiers copi\xE9s : %s.',
	MoveFailedList		: 'Les fichiers suivants ne peuvent \xEAtre d\xE9plac\xE9s :<br />%s',
	CopyFailedList		: 'Les fichiers suivants ne peuvent \xEAtre copi\xE9s :<br />%s',

	// Toolbar Buttons (some used elsewhere)
	Upload		: 'Envoyer',
	UploadTip	: 'Envoyer un nouveau fichier',
	Refresh		: 'Rafra\xEEchir',
	Settings	: 'Configuration',
	Help		: 'Aide',
	HelpTip		: 'Aide',

	// Context Menus
	Select			: 'Choisir',
	SelectThumbnail : 'Choisir une miniature',
	View			: 'Voir',
	Download		: 'T\xE9l\xE9charger',

	NewSubFolder	: 'Nouveau sous-dossier',
	Rename			: 'Renommer',
	Delete			: 'Effacer',
	DeleteFiles		: 'Supprimer les fichiers',

	CopyDragDrop	: 'Copier ici',
	MoveDragDrop	: 'D\xE9placer ici',

	// Dialogs
	RenameDlgTitle		: 'Renommer',
	NewNameDlgTitle		: 'Nouveau fichier',
	FileExistsDlgTitle	: 'Fichier d\xE9j\xE0 existant',
	SysErrorDlgTitle : 'Erreur syst\xE8me',

	FileOverwrite	: 'R\xE9-\xE9crire',
	FileAutorename	: 'Re-nommage automatique',
	ManuallyRename	: 'Renommage manuel',

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Annuler',
	CloseBtn	: 'Fermer',

	// Upload Panel
	UploadTitle			: 'Envoyer un nouveau fichier',
	UploadSelectLbl		: 'S\xE9lectionner le fichier \xE0 t\xE9l\xE9charger',
	UploadProgressLbl	: '(Envoi en cours, veuillez patienter...)',
	UploadBtn			: 'Envoyer le fichier s\xE9lectionn\xE9',
	UploadBtnCancel		: 'Annuler',

	UploadNoFileMsg		: 'S\xE9lectionner un fichier sur votre ordinateur.',
	UploadNoFolder		: 'Merci de s\xE9lectionner un r\xE9pertoire avant l\'envoi.',
	UploadNoPerms		: 'L\'envoi de fichier n\'est pas autoris\xE9.',
	UploadUnknError		: 'Erreur pendant l\'envoi du fichier.',
	UploadExtIncorrect	: 'L\'extension du fichier n\'est pas autoris\xE9e dans ce dossier.',

	// Flash Uploads
	UploadLabel			: 'Fichier \xE0 envoyer',
	UploadTotalFiles	: 'Nombre de fichiers :',
	UploadTotalSize		: 'Poids total :',
	UploadSend			: 'Envoyer',
	UploadAddFiles		: 'Ajouter des fichiers',
	UploadClearFiles	: 'Supprimer les fichiers',
	UploadCancel		: 'Annuler l\'envoi',
	UploadRemove		: 'Retirer',
	UploadRemoveTip		: 'Retirer !f',
	UploadUploaded		: 'T\xE9l\xE9chargement !n%',
	UploadProcessing	: 'Progression...',

	// Settings Panel
	SetTitle		: 'Configuration',
	SetView			: 'Voir :',
	SetViewThumb	: 'Miniatures',
	SetViewList		: 'Liste',
	SetDisplay		: 'Affichage :',
	SetDisplayName	: 'Nom du fichier',
	SetDisplayDate	: 'Date',
	SetDisplaySize	: 'Taille du fichier',
	SetSort			: 'Classement :',
	SetSortName		: 'par nom de fichier',
	SetSortDate		: 'par date',
	SetSortSize		: 'par taille',
	SetSortExtension		: 'par extension de fichier',

	// Status Bar
	FilesCountEmpty : '<Dossier Vide>',
	FilesCountOne	: '1 fichier',
	FilesCountMany	: '%1 fichiers',

	// Size and Speed
	Kb				: '%1 Ko',
	Mb				: '%1 Mo',
	Gb				: '%1 Go',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'La demande n\'a pas abouti. (Erreur %1)',
	Errors :
	{
	 10 : 'Commande invalide.',
	 11 : 'Le type de ressource n\'a pas \xE9t\xE9 sp\xE9cifi\xE9 dans la commande.',
	 12 : 'Le type de ressource n\'est pas valide.',
	102 : 'Nom de fichier ou de dossier invalide.',
	103 : 'La demande n\'a pas abouti : probl\xE8me d\'autorisations.',
	104 : 'La demande n\'a pas abouti : probl\xE8me de restrictions de permissions.',
	105 : 'Extension de fichier invalide.',
	109 : 'Demande invalide.',
	110 : 'Erreur inconnue.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Un fichier ou un dossier avec ce nom existe d\xE9j\xE0.',
	116 : 'Ce dossier n\'existe pas. Veuillez rafra\xEEchir la page et r\xE9essayer.',
	117 : 'Ce fichier n\'existe pas. Veuillez rafra\xEEchir la page et r\xE9essayer.',
	118 : 'Les chemins vers la source et la cible sont les m\xEAmes.',
	201 : 'Un fichier avec ce nom existe d\xE9j\xE0. Le fichier t\xE9l\xE9vers\xE9 a \xE9t\xE9 renomm\xE9 en "%1".',
	202 : 'Fichier invalide.',
	203 : 'Fichier invalide. La taille est trop grande.',
	204 : 'Le fichier t\xE9l\xE9vers\xE9 est corrompu.',
	205 : 'Aucun dossier temporaire n\'est disponible sur le serveur.',
	206 : 'Envoi interrompu pour raisons de s\xE9curit\xE9. Le fichier contient des donn\xE9es de type HTML.',
	207 : 'Le fichier t\xE9l\xE9charg\xE9 a \xE9t\xE9 renomm\xE9 "%1".',
	300 : 'Le d\xE9placement des fichiers a \xE9chou\xE9.',
	301 : 'La copie des fichiers a \xE9chou\xE9.',
	500 : 'L\'interface de gestion des fichiers est d\xE9sactiv\xE9. Contactez votre administrateur et v\xE9rifier le fichier de configuration de CKFinder.',
	501 : 'La fonction "miniatures" est d\xE9sactiv\xE9e.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Le nom du fichier ne peut \xEAtre vide.',
		FileExists		: 'Le fichier %s existes d\xE9j\xE0.',
		FolderEmpty		: 'Le nom du dossier ne peut \xEAtre vide.',
		FolderExists	: 'Le dossier %s existe d\xE9j\xE0.',
		FolderNameExists	: 'Le dossier existe d\xE9j\xE0.',

		FileInvChar		: 'Le nom du fichier ne peut pas contenir les charact\xE8res suivants : \n\\ / : * ? " < > |',
		FolderInvChar	: 'Le nom du dossier ne peut pas contenir les charact\xE8res suivants : \n\\ / : * ? " < > |',

		PopupBlockView	: 'Il n\'a pas \xE9t\xE9 possible d\'ouvrir la nouvelle fen\xEAtre. D\xE9sactiver votre bloqueur de fen\xEAtres pour ce site.',
		XmlError		: 'Impossible de charger correctement la r\xE9ponse XML du serveur web.',
		XmlEmpty		: 'Impossible de charger la r\xE9ponse XML du serveur web. Le serveur a renvoy\xE9 une r\xE9ponse vide.',
		XmlRawResponse	: 'R\xE9ponse du serveur : %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Redimensionner %s',
		sizeTooBig		: 'Impossible de modifier la hauteur ou la largeur de cette image pour une valeur plus grande que l\'original (%size).',
		resizeSuccess	: 'L\'image a \xE9t\xE9 redimensionn\xE9e avec succ\xE8s.',
		thumbnailNew	: 'Cr\xE9er une nouvelle vignette',
		thumbnailSmall	: 'Petit (%s)',
		thumbnailMedium	: 'Moyen (%s)',
		thumbnailLarge	: 'Gros (%s)',
		newSize			: 'D\xE9terminer les nouvelles dimensions',
		width			: 'Largeur',
		height			: 'Hauteur',
		invalidHeight	: 'Hauteur invalide.',
		invalidWidth	: 'Largeur invalide.',
		invalidName		: 'Nom de fichier incorrect.',
		newImage		: 'Cr\xE9er une nouvelle image',
		noExtensionChange : 'L\'extension du fichier ne peut pas \xEAtre chang\xE9.',
		imageSmall		: 'L\'image est trop petit',
		contextMenuName	: 'Redimensionner',
		lockRatio		: 'Conserver les proportions',
		resetSize		: 'Taille d\'origine'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Sauvegarder',
		fileOpenError	: 'Impossible d\'ouvrir le fichier',
		fileSaveSuccess	: 'Fichier sauvegard\xE9 avec succ\xE8s.',
		contextMenuName	: 'Edition',
		loadingFile		: 'Chargement du fichier, veuillez patientez...'
	},

	Maximize :
	{
		maximize : 'Agrandir',
		minimize : 'Minimiser'
	},

	Gallery :
	{
		current : 'Image {current} sur {total}'
	},

	Zip :
	{
		extractHereLabel	: 'D\xE9compresser ici',
		extractToLabel		: 'D\xE9compresser vers...',
		downloadZipLabel	: 'Zipper et t\xE9l\xE9charger',
		compressZipLabel	: 'Zipper',
		removeAndExtract	: 'Supprimer les fichiers existants et d\xE9compresser',
		extractAndOverwrite	: 'D\xE9compresser et remplacer les fichier existants',
		extractSuccess		: 'Les fichiers ont \xE9t\xE9 d\xE9compress\xE9s avec succ\xE8s.'
	},

	Search :
	{
		searchPlaceholder : 'Rechercher'
	}
};
