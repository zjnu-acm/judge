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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Latvian
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['lv'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, unavailable</span>', // MISSING
		confirmCancel	: 'Some of the options were changed. Are you sure you want to close the dialog window?', // MISSING
		ok				: 'Dar\u012Bts!',
		cancel			: 'Atcelt',
		confirmationTitle	: 'Confirmation', // MISSING
		messageTitle	: 'Information', // MISSING
		inputTitle		: 'Question', // MISSING
		undo			: 'Atcelt',
		redo			: 'Atk\u0101rtot',
		skip			: 'Skip', // MISSING
		skipAll			: 'Skip all', // MISSING
		makeDecision	: 'What action should be taken?', // MISSING
		rememberDecision: 'Remember my decision' // MISSING
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'lv',

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
	FoldersTitle	: 'Mapes',
	FolderLoading	: 'Iel\u0101d\u0113...',
	FolderNew		: 'L\u016Bdzu ierakstiet mapes nosaukumu: ',
	FolderRename	: 'L\u016Bdzu ierakstiet jauno mapes nosaukumu: ',
	FolderDelete	: 'Vai tie\u0161\u0101m v\u0113laties neatgriezeniski dz\u0113st mapi "%1"?',
	FolderRenaming	: ' (P\u0101rsauc...)',
	FolderDeleting	: ' (Dz\u0113\u0161...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'L\u016Bdzu ierakstiet jauno faila nosaukumu: ',
	FileRenameExt	: 'Vai tie\u0161\u0101m v\u0113laties main\u012Bt faila papla\u0161in\u0101jumu? Fails var palikt nelietojams.',
	FileRenaming	: 'P\u0101rsauc...',
	FileDelete		: 'Vai tie\u0161\u0101m v\u0113laties neatgriezeniski dz\u0113st failu "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Iel\u0101d\u0113...',
	FilesEmpty		: 'The folder is empty.', // MISSING
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Basket', // MISSING
	BasketClear			: 'Clear Basket', // MISSING
	BasketRemove		: 'Remove from Basket', // MISSING
	BasketOpenFolder	: 'Open Parent Folder', // MISSING
	BasketTruncateConfirm : 'Do you really want to remove all files from the basket?', // MISSING
	BasketRemoveConfirm	: 'Do you really want to remove the file "%1" from the basket?', // MISSING
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'No files in the basket, drag and drop some.', // MISSING
	BasketCopyFilesHere	: 'Copy Files from Basket', // MISSING
	BasketMoveFilesHere	: 'Move Files from Basket', // MISSING

	// Global messages
	OperationCompletedSuccess	: 'Operation completed successfully.', // MISSING
	OperationCompletedErrors		: 'Operation completed with errors.', // MISSING
	FileError				: '%s: %e', // MISSING

	// Move and Copy files
	MovedFilesNumber		: 'Number of files moved: %s.', // MISSING
	CopiedFilesNumber	: 'Number of files copied: %s.', // MISSING
	MoveFailedList		: 'The following files could not be moved:<br />%s', // MISSING
	CopyFailedList		: 'The following files could not be copied:<br />%s', // MISSING

	// Toolbar Buttons (some used elsewhere)
	Upload		: 'Aug\u0161upiel\u0101d\u0113t',
	UploadTip	: 'Aug\u0161upiel\u0101d\u0113t jaunu failu',
	Refresh		: 'P\u0101rl\u0101d\u0113t',
	Settings	: 'Uzst\u0101d\u012Bjumi',
	Help		: 'Pal\u012Bdz\u012Bba',
	HelpTip		: 'Pal\u012Bdz\u012Bba',

	// Context Menus
	Select			: 'Izv\u0113l\u0113ties',
	SelectThumbnail : 'Izv\u0113l\u0113ties s\u012Bkbildi',
	View			: 'Skat\u012Bt',
	Download		: 'Lejupiel\u0101d\u0113t',

	NewSubFolder	: 'Jauna apak\u0161mape',
	Rename			: 'P\u0101rsaukt',
	Delete			: 'Dz\u0113st',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Copy Here', // MISSING
	MoveDragDrop	: 'Move Here', // MISSING

	// Dialogs
	RenameDlgTitle		: 'Rename', // MISSING
	NewNameDlgTitle		: 'New Name', // MISSING
	FileExistsDlgTitle	: 'File Already Exists', // MISSING
	SysErrorDlgTitle : 'System Error', // MISSING

	FileOverwrite	: 'Overwrite', // MISSING
	FileAutorename	: 'Auto-rename', // MISSING
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'Labi',
	CancelBtn	: 'Atcelt',
	CloseBtn	: 'Aizv\u0113rt',

	// Upload Panel
	UploadTitle			: 'Jauna faila aug\u0161upiel\u0101d\u0113\u0161ana',
	UploadSelectLbl		: 'Izv\u0113laties failu, ko aug\u0161upiel\u0101d\u0113t',
	UploadProgressLbl	: '(Aug\u0161upiel\u0101d\u0113, l\u016Bdzu uzgaidiet...)',
	UploadBtn			: 'Aug\u0161upiel\u0101d\u0113t izv\u0113l\u0113to failu',
	UploadBtnCancel		: 'Atcelt',

	UploadNoFileMsg		: 'L\u016Bdzu izv\u0113laties failu no sava datora.',
	UploadNoFolder		: 'Please select a folder before uploading.', // MISSING
	UploadNoPerms		: 'File upload not allowed.', // MISSING
	UploadUnknError		: 'Error sending the file.', // MISSING
	UploadExtIncorrect	: 'File extension not allowed in this folder.', // MISSING

	// Flash Uploads
	UploadLabel			: 'Files to Upload', // MISSING
	UploadTotalFiles	: 'Total Files:', // MISSING
	UploadTotalSize		: 'Total Size:', // MISSING
	UploadSend			: 'Aug\u0161upiel\u0101d\u0113t',
	UploadAddFiles		: 'Add Files', // MISSING
	UploadClearFiles	: 'Clear Files', // MISSING
	UploadCancel		: 'Cancel Upload', // MISSING
	UploadRemove		: 'Remove', // MISSING
	UploadRemoveTip		: 'Remove !f', // MISSING
	UploadUploaded		: 'Uploaded !n%', // MISSING
	UploadProcessing	: 'Processing...', // MISSING

	// Settings Panel
	SetTitle		: 'Uzst\u0101d\u012Bjumi',
	SetView			: 'Att\u0113lot:',
	SetViewThumb	: 'S\u012Bkbildes',
	SetViewList		: 'Failu Sarakstu',
	SetDisplay		: 'R\u0101d\u012Bt:',
	SetDisplayName	: 'Faila Nosaukumu',
	SetDisplayDate	: 'Datumu',
	SetDisplaySize	: 'Faila Izm\u0113ru',
	SetSort			: 'K\u0101rtot:',
	SetSortName		: 'p\u0113c Faila Nosaukuma',
	SetSortDate		: 'p\u0113c Datuma',
	SetSortSize		: 'p\u0113c Izm\u0113ra',
	SetSortExtension		: 'by Extension', // MISSING

	// Status Bar
	FilesCountEmpty : '<Tuk\u0161a mape>',
	FilesCountOne	: '1 fails',
	FilesCountMany	: '%1 faili',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Nebija iesp\u0113jams pabeigt piepras\u012Bjumu. (K\u013C\u016Bda %1)',
	Errors :
	{
	 10 : 'Neder\u012Bga komanda.',
	 11 : 'Resursa veids netika nor\u0101d\u012Bts piepras\u012Bjum\u0101.',
	 12 : 'Piepras\u012Btais resursa veids nav der\u012Bgs.',
	102 : 'Neder\u012Bgs faila vai mapes nosaukums.',
	103 : 'Nav iesp\u0113jams pabeigt piepras\u012Bjumu, autoriz\u0101cijas aizliegumu d\u0113\u013C.',
	104 : 'Nav iesp\u0113jams pabeigt piepras\u012Bjumu, failu sist\u0113mas at\u013Cauju ierobe\u017Eojumu d\u0113\u013C.',
	105 : 'Neat\u013Cauts faila papla\u0161in\u0101jums.',
	109 : 'Neder\u012Bgs piepras\u012Bjums.',
	110 : 'Nezin\u0101ma k\u013C\u016Bda.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Fails vai mape ar \u0161\u0101du nosaukumu jau past\u0101v.',
	116 : 'Mape nav atrasta. L\u016Bdzu p\u0101rl\u0101d\u0113jiet \u0161o logu un m\u0113\u0123iniet v\u0113lreiz.',
	117 : 'Fails nav atrasts. L\u016Bdzu p\u0101rl\u0101d\u0113jiet failu sarakstu un m\u0113\u0123iniet v\u0113lreiz.',
	118 : 'Source and target paths are equal.', // MISSING
	201 : 'Fails ar \u0161\u0101du nosaukumu jau eksist\u0113. Aug\u0161upiel\u0101d\u0113tais fails tika p\u0101rsaukts par "%1".',
	202 : 'Neder\u012Bgs fails.',
	203 : 'Neder\u012Bgs fails. Faila izm\u0113rs p\u0101rsniedz pie\u013Caujamo.',
	204 : 'Aug\u0161upiel\u0101d\u0113tais fails ir boj\u0101ts.',
	205 : 'Neviena pagaidu mape nav pieejama priek\u0161 aug\u0161upiel\u0101d\u0113\u0161anas uz servera.',
	206 : 'Aug\u0161upiel\u0101de atcelta dro\u0161\u012Bbas apsv\u0113rumu d\u0113\u013C. Fails satur HTML veida datus.',
	207 : 'Aug\u0161upiel\u0101d\u0113tais fails tika p\u0101rsaukts par "%1".',
	300 : 'Moving file(s) failed.', // MISSING
	301 : 'Copying file(s) failed.', // MISSING
	500 : 'Failu p\u0101rl\u016Bks ir atsl\u0113gts dro\u0161\u012Bbas apsv\u0113rumu d\u0113\u013C. L\u016Bdzu sazinieties ar \u0161\u012Bs sist\u0113mas tehnisko administratoru vai p\u0101rbaudiet CKFinder konfigur\u0101cijas failu.',
	501 : 'S\u012Bkbil\u017Eu atbalsts ir atsl\u0113gts.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Faila nosaukum\u0101 nevar b\u016Bt tuk\u0161ums.',
		FileExists		: 'File %s already exists.', // MISSING
		FolderEmpty		: 'Mapes nosaukum\u0101 nevar b\u016Bt tuk\u0161ums.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Faila nosaukums nedr\u012Bkst satur\u0113t nevienu no sekojo\u0161aj\u0101m z\u012Bm\u0113m: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Mapes nosaukums nedr\u012Bkst satur\u0113t nevienu no sekojo\u0161aj\u0101m z\u012Bm\u0113m: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Nav iesp\u0113jams failu atv\u0113rt jaun\u0101 log\u0101. L\u016Bdzu veiciet izmai\u0146as uzst\u0101d\u012Bjumos savai interneta p\u0101rl\u016Bkprogrammai un izsl\u0113dziet visus uznirsto\u0161o logu blo\u0137\u0113t\u0101jus \u0161ai adresei.',
		XmlError		: 'It was not possible to properly load the XML response from the web server.', // MISSING
		XmlEmpty		: 'It was not possible to load the XML response from the web server. The server returned an empty response.', // MISSING
		XmlRawResponse	: 'Raw response from the server: %s' // MISSING
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Resize %s', // MISSING
		sizeTooBig		: 'Cannot set image height or width to a value bigger than the original size (%size).', // MISSING
		resizeSuccess	: 'Image resized successfully.', // MISSING
		thumbnailNew	: 'Create a new thumbnail', // MISSING
		thumbnailSmall	: 'Small (%s)', // MISSING
		thumbnailMedium	: 'Medium (%s)', // MISSING
		thumbnailLarge	: 'Large (%s)', // MISSING
		newSize			: 'Set a new size', // MISSING
		width			: 'Platums',
		height			: 'Augstums',
		invalidHeight	: 'Invalid height.', // MISSING
		invalidWidth	: 'Invalid width.', // MISSING
		invalidName		: 'Invalid file name.', // MISSING
		newImage		: 'Create a new image', // MISSING
		noExtensionChange : 'File extension cannot be changed.', // MISSING
		imageSmall		: 'Source image is too small.', // MISSING
		contextMenuName	: 'Resize', // MISSING
		lockRatio		: 'Nemain\u012Bga Augstuma/Platuma attiec\u012Bba',
		resetSize		: 'Atjaunot s\u0101kotn\u0113jo izm\u0113ru'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Saglab\u0101t',
		fileOpenError	: 'Unable to open file.', // MISSING
		fileSaveSuccess	: 'File saved successfully.', // MISSING
		contextMenuName	: 'Edit', // MISSING
		loadingFile		: 'Loading file, please wait...' // MISSING
	},

	Maximize :
	{
		maximize : 'Maximize', // MISSING
		minimize : 'Minimize' // MISSING
	},

	Gallery :
	{
		current : 'Image {current} of {total}' // MISSING
	},

	Zip :
	{
		extractHereLabel	: 'Extract here', // MISSING
		extractToLabel		: 'Extract to...', // MISSING
		downloadZipLabel	: 'Download as zip', // MISSING
		compressZipLabel	: 'Compress to zip', // MISSING
		removeAndExtract	: 'Remove existing and extract', // MISSING
		extractAndOverwrite	: 'Extract overwriting existing files', // MISSING
		extractSuccess		: 'File extracted successfully.' // MISSING
	},

	Search :
	{
		searchPlaceholder : 'Mekl\u0113t'
	}
};
