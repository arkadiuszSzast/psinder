package com.psinder.auth.account

import com.psinder.auth.role.HasRole

interface AccountContext : AccountIdProvider, HasRole
