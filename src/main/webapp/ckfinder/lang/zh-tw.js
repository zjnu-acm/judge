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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Chinese (Taiwan)
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['zh-tw'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, unavailable</span>', // MISSING
		confirmCancel	: 'Some of the options were changed. Are you sure you want to close the dialog window?', // MISSING
		ok				: 'OK', // MISSING
		cancel			: 'Cancel', // MISSING
		confirmationTitle	: 'Confirmation', // MISSING
		messageTitle	: 'Information', // MISSING
		inputTitle		: 'Question', // MISSING
		undo			: 'Undo', // MISSING
		redo			: 'Redo', // MISSING
		skip			: 'Skip', // MISSING
		skipAll			: 'Skip all', // MISSING
		makeDecision	: 'What action should be taken?', // MISSING
		rememberDecision: 'Remember my decision' // MISSING
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'zh-tw',
	LangCode : 'zh-tw',

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
	DateTime : 'mm/dd/yyyy HH:MM',
	DateAmPm : ['\u4E0A\u5348', '\u4E0B\u5348'],

	// Folders
	FoldersTitle	: '\u76EE\u9304',
	FolderLoading	: '\u8F09\u5165\u4E2D...',
	FolderNew		: '\u8ACB\u8F38\u5165\u65B0\u76EE\u9304\u540D\u7A31: ',
	FolderRename	: '\u8ACB\u8F38\u5165\u65B0\u76EE\u9304\u540D\u7A31: ',
	FolderDelete	: '\u78BA\u5B9A\u522A\u9664 "%1" \u9019\u500B\u76EE\u9304\u55CE?',
	FolderRenaming	: ' (\u4FEE\u6539\u76EE\u9304...)',
	FolderDeleting	: ' (\u522A\u9664\u76EE\u9304...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: '\u8ACB\u8F38\u5165\u65B0\u6A94\u6848\u540D\u7A31: ',
	FileRenameExt	: '\u78BA\u5B9A\u8B8A\u66F4\u9019\u500B\u6A94\u6848\u7684\u526F\u6A94\u540D\u55CE? \u8B8A\u66F4\u5F8C , \u6B64\u6A94\u6848\u53EF\u80FD\u6703\u7121\u6CD5\u4F7F\u7528 !',
	FileRenaming	: '\u4FEE\u6539\u6A94\u6848\u540D\u7A31...',
	FileDelete		: '\u78BA\u5B9A\u8981\u522A\u9664\u9019\u500B\u6A94\u6848 "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: '\u8F09\u5165\u4E2D...',
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
	Upload		: '\u4E0A\u50B3\u6A94\u6848',
	UploadTip	: '\u4E0A\u50B3\u4E00\u500B\u65B0\u6A94\u6848',
	Refresh		: '\u91CD\u65B0\u6574\u7406',
	Settings	: '\u504F\u597D\u8A2D\u5B9A',
	Help		: '\u8AAA\u660E',
	HelpTip		: '\u8AAA\u660E',

	// Context Menus
	Select			: '\u9078\u64C7',
	SelectThumbnail : 'Select Thumbnail', // MISSING
	View			: '\u700F\u89BD',
	Download		: '\u4E0B\u8F09',

	NewSubFolder	: '\u5EFA\u7ACB\u65B0\u5B50\u76EE\u9304',
	Rename			: '\u91CD\u65B0\u547D\u540D',
	Delete			: '\u522A\u9664',
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
	OkBtn		: '\u78BA\u5B9A',
	CancelBtn	: '\u53D6\u6D88',
	CloseBtn	: '\u95DC\u9589',

	// Upload Panel
	UploadTitle			: '\u4E0A\u50B3\u65B0\u6A94\u6848',
	UploadSelectLbl		: '\u8ACB\u9078\u64C7\u8981\u4E0A\u50B3\u7684\u6A94\u6848',
	UploadProgressLbl	: '(\u6A94\u6848\u4E0A\u50B3\u4E2D , \u8ACB\u7A0D\u5019...)',
	UploadBtn			: '\u5C07\u6A94\u6848\u4E0A\u50B3\u5230\u4F3A\u670D\u5668',
	UploadBtnCancel		: '\u53D6\u6D88',

	UploadNoFileMsg		: '\u8ACB\u5F9E\u4F60\u7684\u96FB\u8166\u9078\u64C7\u4E00\u500B\u6A94\u6848.',
	UploadNoFolder		: 'Please select a folder before uploading.', // MISSING
	UploadNoPerms		: 'File upload not allowed.', // MISSING
	UploadUnknError		: 'Error sending the file.', // MISSING
	UploadExtIncorrect	: 'File extension not allowed in this folder.', // MISSING

	// Flash Uploads
	UploadLabel			: 'Files to Upload', // MISSING
	UploadTotalFiles	: 'Total Files:', // MISSING
	UploadTotalSize		: 'Total Size:', // MISSING
	UploadSend			: '\u4E0A\u50B3\u6A94\u6848',
	UploadAddFiles		: 'Add Files', // MISSING
	UploadClearFiles	: 'Clear Files', // MISSING
	UploadCancel		: 'Cancel Upload', // MISSING
	UploadRemove		: 'Remove', // MISSING
	UploadRemoveTip		: 'Remove !f', // MISSING
	UploadUploaded		: 'Uploaded !n%', // MISSING
	UploadProcessing	: 'Processing...', // MISSING

	// Settings Panel
	SetTitle		: '\u8A2D\u5B9A',
	SetView			: '\u700F\u89BD\u65B9\u5F0F:',
	SetViewThumb	: '\u7E2E\u5716\u9810\u89BD',
	SetViewList		: '\u6E05\u55AE\u5217\u8868',
	SetDisplay		: '\u986F\u793A\u6B04\u4F4D:',
	SetDisplayName	: '\u6A94\u6848\u540D\u7A31',
	SetDisplayDate	: '\u6A94\u6848\u65E5\u671F',
	SetDisplaySize	: '\u6A94\u6848\u5927\u5C0F',
	SetSort			: '\u6392\u5E8F\u65B9\u5F0F:',
	SetSortName		: '\u4F9D \u6A94\u6848\u540D\u7A31',
	SetSortDate		: '\u4F9D \u6A94\u6848\u65E5\u671F',
	SetSortSize		: '\u4F9D \u6A94\u6848\u5927\u5C0F',
	SetSortExtension		: 'by Extension', // MISSING

	// Status Bar
	FilesCountEmpty : '<\u6B64\u76EE\u9304\u6C92\u6709\u4EFB\u4F55\u6A94\u6848>',
	FilesCountOne	: '1 \u500B\u6A94\u6848',
	FilesCountMany	: '%1 \u500B\u6A94\u6848',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB', // MISSING
	Gb				: '%1 GB', // MISSING
	SizePerSecond	: '%1/s', // MISSING

	// Connector Error Messages.
	ErrorUnknown	: '\u7121\u6CD5\u9023\u63A5\u5230\u4F3A\u670D\u5668 ! (\u932F\u8AA4\u4EE3\u78BC %1)',
	Errors :
	{
	 10 : '\u4E0D\u5408\u6CD5\u7684\u6307\u4EE4.',
	 11 : '\u9023\u63A5\u904E\u7A0B\u4E2D , \u672A\u6307\u5B9A\u8CC7\u6E90\u5F62\u614B !',
	 12 : '\u9023\u63A5\u904E\u7A0B\u4E2D\u51FA\u73FE\u4E0D\u5408\u6CD5\u7684\u8CC7\u6E90\u5F62\u614B !',
	102 : '\u4E0D\u5408\u6CD5\u7684\u6A94\u6848\u6216\u76EE\u9304\u540D\u7A31 !',
	103 : '\u7121\u6CD5\u9023\u63A5\uFF1A\u53EF\u80FD\u662F\u4F7F\u7528\u8005\u6B0A\u9650\u8A2D\u5B9A\u932F\u8AA4 !',
	104 : '\u7121\u6CD5\u9023\u63A5\uFF1A\u53EF\u80FD\u662F\u4F3A\u670D\u5668\u6A94\u6848\u6B0A\u9650\u8A2D\u5B9A\u932F\u8AA4 !',
	105 : '\u7121\u6CD5\u4E0A\u50B3\uFF1A\u4E0D\u5408\u6CD5\u7684\u526F\u6A94\u540D !',
	109 : '\u4E0D\u5408\u6CD5\u7684\u8ACB\u6C42 !',
	110 : '\u4E0D\u660E\u932F\u8AA4 !',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : '\u6A94\u6848\u6216\u76EE\u9304\u540D\u7A31\u91CD\u8907 !',
	116 : '\u627E\u4E0D\u5230\u76EE\u9304 ! \u8ACB\u5148\u91CD\u65B0\u6574\u7406 , \u7136\u5F8C\u518D\u8A66\u4E00\u6B21 !',
	117 : '\u627E\u4E0D\u5230\u6A94\u6848 ! \u8ACB\u5148\u91CD\u65B0\u6574\u7406 , \u7136\u5F8C\u518D\u8A66\u4E00\u6B21 !',
	118 : 'Source and target paths are equal.', // MISSING
	201 : '\u4F3A\u670D\u5668\u4E0A\u5DF2\u6709\u76F8\u540C\u7684\u6A94\u6848\u540D\u7A31 ! \u60A8\u4E0A\u50B3\u7684\u6A94\u6848\u540D\u7A31\u5C07\u6703\u81EA\u52D5\u66F4\u6539\u70BA "%1".',
	202 : '\u4E0D\u5408\u6CD5\u7684\u6A94\u6848 !',
	203 : '\u4E0D\u5408\u6CD5\u7684\u6A94\u6848 ! \u6A94\u6848\u5927\u5C0F\u8D85\u904E\u9810\u8A2D\u503C !',
	204 : '\u60A8\u4E0A\u50B3\u7684\u6A94\u6848\u5DF2\u7D93\u640D\u6BC0 !',
	205 : '\u4F3A\u670D\u5668\u4E0A\u6C92\u6709\u9810\u8A2D\u7684\u66AB\u5B58\u76EE\u9304 !',
	206 : '\u6A94\u6848\u4E0A\u50B3\u7A0B\u5E8F\u56E0\u70BA\u5B89\u5168\u56E0\u7D20\u5DF2\u88AB\u7CFB\u7D71\u81EA\u52D5\u53D6\u6D88 ! \u53EF\u80FD\u662F\u4E0A\u50B3\u7684\u6A94\u6848\u5167\u5BB9\u5305\u542B HTML \u78BC !',
	207 : '\u60A8\u4E0A\u50B3\u7684\u6A94\u6848\u540D\u7A31\u5C07\u6703\u81EA\u52D5\u66F4\u6539\u70BA "%1".',
	300 : 'Moving file(s) failed.', // MISSING
	301 : 'Copying file(s) failed.', // MISSING
	500 : '\u56E0\u70BA\u5B89\u5168\u56E0\u7D20 , \u6A94\u6848\u700F\u89BD\u5668\u5DF2\u88AB\u505C\u7528 ! \u8ACB\u806F\u7D61\u60A8\u7684\u7CFB\u7D71\u7BA1\u7406\u8005\u4E26\u6AA2\u67E5 CKFinder \u7684\u8A2D\u5B9A\u6A94 config.php !',
	501 : '\u7E2E\u5716\u9810\u89BD\u529F\u80FD\u5DF2\u88AB\u505C\u7528 !'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: '\u6A94\u6848\u540D\u7A31\u4E0D\u80FD\u7A7A\u767D !',
		FileExists		: 'File %s already exists.', // MISSING
		FolderEmpty		: '\u76EE\u9304\u540D\u7A31\u4E0D\u80FD\u7A7A\u767D !',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: '\u6A94\u6848\u540D\u7A31\u4E0D\u80FD\u5305\u542B\u4EE5\u4E0B\u5B57\u5143\uFF1A \n\\ / : * ? " < > |',
		FolderInvChar	: '\u76EE\u9304\u540D\u7A31\u4E0D\u80FD\u5305\u542B\u4EE5\u4E0B\u5B57\u5143\uFF1A \n\\ / : * ? " < > |',

		PopupBlockView	: '\u7121\u6CD5\u5728\u65B0\u8996\u7A97\u958B\u555F\u6A94\u6848 ! \u8ACB\u6AA2\u67E5\u700F\u89BD\u5668\u7684\u8A2D\u5B9A\u4E26\u4E14\u91DD\u5C0D\u9019\u500B\u7DB2\u7AD9 \u95DC\u9589 <\u5C01\u9396\u5F48\u8DF3\u8996\u7A97> \u9019\u500B\u529F\u80FD !',
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
		width			: 'Width', // MISSING
		height			: 'Height', // MISSING
		invalidHeight	: 'Invalid height.', // MISSING
		invalidWidth	: 'Invalid width.', // MISSING
		invalidName		: 'Invalid file name.', // MISSING
		newImage		: 'Create a new image', // MISSING
		noExtensionChange : 'File extension cannot be changed.', // MISSING
		imageSmall		: 'Source image is too small.', // MISSING
		contextMenuName	: 'Resize', // MISSING
		lockRatio		: 'Lock ratio', // MISSING
		resetSize		: 'Reset size' // MISSING
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Save', // MISSING
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
		searchPlaceholder : '\u641C\u5C0B'
	}
};
