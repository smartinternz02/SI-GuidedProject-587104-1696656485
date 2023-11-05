package com.aarush.core.data.remote.mapper

import com.aarush.core.abstraction.BaseMapper
import com.aarush.core.data.remote.response.SourcesResponse
import com.aarush.core.model.model.Source
import com.aarush.core.util.extension.NOT_AVAILABLE

class SourcesMapper : BaseMapper<SourcesResponse, List<Source>> {

    override fun mapToDomain(raw: SourcesResponse): List<Source> {
        return raw.sources?.map {
            Source(it?.id ?: NOT_AVAILABLE, it?.name ?: NOT_AVAILABLE, it?.url ?: NOT_AVAILABLE, it?.description ?: NOT_AVAILABLE, it?.category ?: NOT_AVAILABLE)
        } ?: emptyList()
    }

    override fun mapToRaw(domain: List<Source>): SourcesResponse {
        return SourcesResponse()
    }


}