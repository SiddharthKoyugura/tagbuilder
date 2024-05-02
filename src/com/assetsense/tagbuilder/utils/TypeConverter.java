package com.assetsense.tagbuilder.utils;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.c2.domain.Lookup;
import com.assetsense.tagbuilder.c2.domain.Observation;
import com.assetsense.tagbuilder.c2.domain.Tag;
import com.assetsense.tagbuilder.dto.AssetDTO;
import com.assetsense.tagbuilder.dto.ObservationDTO;
import com.assetsense.tagbuilder.dto.TagDTO;

public class TypeConverter {
	public AssetDTO convertToAssetDTO(Asset asset){
		AssetDTO assetDTO = new AssetDTO();
        assetDTO.setId(asset.getId());
        assetDTO.setName(asset.getName());
        assetDTO.setEcn(asset.getEcn());
        assetDTO.setLocation(asset.getLocation());
        assetDTO.setCategory(asset.getCategory());
        assetDTO.setIsCompleted(asset.getIsCompleted());

        Lookup model = asset.getModel();
        if (model != null) {
            assetDTO.setModel(model);
        }

        Lookup assettype = asset.getAssettype();
        if (assettype != null) {
            assetDTO.setAssettype(assettype);
        }

        Lookup supplierName = asset.getSupplierName();
        if (supplierName != null) {
            assetDTO.setSupplierName(supplierName);
        }

        for (Asset childAsset : asset.getChildAssets()) {
        	assetDTO.getChildAssets().add(convertToAssetDTO(childAsset));
        }

        for (Observation observation : asset.getObservations()) {
            assetDTO.getObservations().add(convertToObservationDTO(observation));
        }

        return assetDTO;
	}
	
	public ObservationDTO convertToObservationDTO(Observation observation){
		ObservationDTO obsDTO = new ObservationDTO();
		
		obsDTO.setId(observation.getId());
		obsDTO.setCode(observation.getCode());
		obsDTO.setInputType(observation.getInputType());
		obsDTO.setDescription(observation.getDescription());
		obsDTO.setCategory(observation.getCategory());
		
		return obsDTO;
	}
	
	public TagDTO convertToTagDTO(Tag tag) {
		TagDTO tagDTO = new TagDTO();
		
		tagDTO.setId(tag.getId());
		tagDTO.setName(tag.getName());
		tagDTO.setCategory(tag.getCategory());
		tagDTO.setAsset(convertToAssetDTO(tag.getAsset()));
		tagDTO.setObservation(convertToObservationDTO(tag.getObservation()));
		
		return tagDTO;
	}
}
