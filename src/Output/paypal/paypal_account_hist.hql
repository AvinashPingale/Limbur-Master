ALTER TABLE XX_INTEGRATION_DB_XX.ACCOUNT ADD IF NOT EXISTS PARTITION (year = 'XX_YYYY_XX', month = 'XX_MM_XX, day = 'XX_DD_XX', partition_type = 'XX_BASE_XX', seqnum='48');

INSERT OVERWRITE TABLE XX_INTEGRATION_DB_XX.ACCOUNT PARTITION (year = 'XX_YYYY_XX', month = 'XX_MM_XX, day = 'XX_DD_XX', partition_type = 'XX_BASE_XX', seqnum='48')
	 SELECT 
		coalesce(account_xref.account_key, -99) as account_key
		'#' as account_id
		'#' as account_id_uuid
		'#' as exposed_account_uuid
		-99.0 as product_key
		type as account_type_code
		'#' as account_subtype_code
		TWO TABLE JOIN as pricing_category_code
		TWO TABLE JOIN as pricing_subcategory_code
		'#' as co_brand_code
		cast(from_unixtime(account.time_created) as timestamp) as source_created_timestamp
		cast(from_unixtime(account.time_updated) as timestamp) as source_updated_timestamp
		'#' as email_address
		'#' as primary_value_type_code
		TWO TABLE JOIN as primary_currency_code
		'ORIGINAL_COLUMN' as original_country_code
		INTERIM JOIN as country_code
		'#' as legal_country_code
		'#' as original_state_name
		'#' as state_name
		TWO TABLE JOIN as legal_company_id
		'#' as security_level_code
		cast(from_unixtime(account.last_time_opened) as timestamp) as last_opened_timestamp
		cast(from_unixtime(account.last_time_closed) as timestamp) as last_closed_timestamp
		'#' as sign_up_type_code
		'#' as sign_up_ip_address
		'#' as sign_up_user_agent_string
		'#' as sign_up_url
		'#' as sign_up_device_type_code
		-99.0 as sign_up_device_key
		-99.0 as sign_up_marketing_tactic_key
		'#' as sign_up_status_code
		'3999-12-31 23=59=59' as last_login_timestamp
		'#' as marketing_cookie_value
		'#' as test_cookie_value
		'#' as tracking_cookie_value
		'#' as origination_channel_code
		'#' as operational_type_code
		'#' as guest_account_type_code
		-99.0 as social_score
		-99.0 as custom_score
		'#' as sar_filing_number
		'#' as restriction_reason_code
		'#' as account_status_code
		'3999-12-31 23=59=59' as account_status_timestamp
		'3999-12-31 23=59=59' as source_deleted_timestamp
		'#' as timezone_code
		'#' as credential_type_code
		'#' as credential_value_uuid
		'#' as password_uuid
		'#' as friends_invitation_code
		'3999-12-31 23=59=59' as first_contact_sync_timestamp
		'3999-12-31 23=59=59' as last_contact_sync_timestamp
		'#' as memo
		-99.0 as controlling_account_key
		'PULL FROM FLAGS TAB' 'TWO TABLE JOIN' as account_flags
		1.0 as servicing_company_id
		coalesce(account_xref.date_created,cast(date_format('9999-12-31 23:59:59', 'yyyy-MM-dd HH:mm:ss') as timestamp)) as row_created_timestamp
		current_timestamp as row_updated_timestamp
FROM 
	XX_RAW_IDENTITY_DB_XX.account account
LEFT OUTER JOIN 
